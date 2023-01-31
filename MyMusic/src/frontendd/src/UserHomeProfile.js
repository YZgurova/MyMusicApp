import React, {useState, useEffect, useDebugValue} from 'react'
import {useNavigate} from "react-router-dom";
import {
    Breadcrumb, Button,
    Empty, Input,
    Layout,
    Menu, message, Modal,
    Spin,
    Table,
    Typography
} from 'antd';
import {
    HeartFilled, HeartOutlined,
    LoadingOutlined,
    PlayCircleOutlined,
    UserOutlined
} from '@ant-design/icons';
import {BsMusicNoteBeamed} from "react-icons/bs";
import Stripe from "react-stripe-checkout";
import axios from "axios";

const {Header, Content, Footer, Sider} =Layout;

process.env.REACT_APP_AWS_ACCESS_KEY_ID = "AKIA2RF2TVJI3MTYMY6V"
process.env.REACT_APP_AWS_SECRET_ACCESS_KEY = "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"

const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

const save = () => {
    message.success("Save successful").then();
}

const audioMap = new Map()
const urlsMap = new Map()

function UserHomeProfile() {
    const [songList, setSongList] = useState([]);
    const [favSongs, setFavSongs] = useState([]);
    const [boughtSongs, setBoughtSongs] = useState([]);
    const [isListVisible, setIsListVisible] = useState(true);
    const [isFavVisible, setIsFavVisible] = useState(false);
    const [isBoughtVisible, setIsBoughtVisible] = useState(false);
    const [inputId, setInputId] = useState('');
    const [collapsed, setCollapsed] = useState(false);
    const [fetching, setFetching] = useState(true);
    const [selectedRow, setSelectedRow] = useState({});
    const [selectedRowB, setSelectedRowB] = useState({});
    const [isUpdated, setIsUpdated] = useState(false);
    const [loading, setLoading] = useState(false);
    const [audioUrl, setAudioUrl] = useState(null);
    const [isPlayed, setIsPlayed] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const navigate = useNavigate();

    const showModal = () => {
        const roles = JSON.parse(localStorage.getItem("roles"));
        if(roles.includes("CREATOR")) {
            navigate("/creator");
        } else {
            setIsModalOpen(true);
        }
    };

    const handleOk = () => {
        if(inputId === localStorage.getItem("id")) {
            setIsModalOpen(false);
            if (createCreator().then(r => {return r.ok;})) {
                localStorage.setItem("roles", JSON.stringify(["USER", "CREATOR"]));
                navigate("/creator")
            } else {
                message.error("Sorry, we can not create your profile now, but will connect with you later!")
            }
        }
        else {
            message.error("This is not your Id, please check it in your personal data!")
        }
    };

    const createCreator = async () => {
        return await fetch(`http://localhost:8080/api/user`,{
            method: 'POST',
            mode: 'cors',
            headers: {
                 'Authorization':`Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify({
                userId: localStorage.getItem("id")
            })
        });
    }

    const handleCancel = () => {
        setIsModalOpen(false);
    };

    function handleClickLove(record){
        if(record.isLoved) {
            unSaveSong(record).then();
        } else {
            saveSong(record).then();
        }
        setIsUpdated(!isUpdated);
    }

    function handleClickPlay(record){
        console.log(record);
        setSelectedRow(record);
        setIsPlayed(urlsMap.set(record.id, !urlsMap.get(record.id)));
        handleAudioPlay(record.id, record.url, !urlsMap.get(record.id), record.isBought);
    }

    const handleAudioPlay = (recordId, s3_url_link, state, isBought) => {
        debugger;
        console.log(s3_url_link);
        if(audioMap.get(recordId)==null) {
            var audio = new Audio(s3_url_link);
            audioMap.set(recordId, audio);
        }
        var audio=audioMap.get(recordId);
        console.log(state);
        if (!state) {
            audio.currentTime=40;
            audio.play();
            if(!isBought) {
                setInterval(function(){
                    if(audio.currentTime>45){
                        audio.pause();
                    }
                },700);
                message.warning("Please pay if you want to sound all song")
            }
        } else {
            audio.pause();
            audio.currentTime=0;
        }
    };

    async function handleToken(token) {
        console.log(token.id)
        debugger;
        await axios.post("http://localhost:8080/api/user/charge", "", {
            headers: {
                'Content-Type':'application/json',
                'Authorization':`Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify({
                amount: 500,
                token: token.id
            })

        }).then(() => {
            alert("Payment Success");
        }).catch((error) => {
            alert("Payment Unsuccessful");
        });
    }

    const handleToken2 = async (token) => {
        console.log(token);
        console.log(token.id);
        console.log(selectedRowB);
        console.log(selectedRowB.price);
        debugger;
        const result = await fetch("http://localhost:8080/api/user/charge", {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Authorization':`Bearer ${localStorage.getItem("token")}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                songId: selectedRowB.id,
                amount: selectedRowB.price*100,
                token: token.id,
                songName: selectedRowB.name
            })
        });
        console.log(result);
        if(result.status===201) {
            message.success("Success payment");
        } else {
            message.error("Unsuccessful payment")
        }
        setIsUpdated(!isUpdated);
    }

    const saveSong = async (record) => {
        debugger;
        return (await fetch(`http://localhost:8080/api/song/${record.id}/like`, {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        })).json();
    }

    const unSaveSong = async (record) => {
        debugger;
        return await fetch(`http://localhost:8080/api/song/${record.id}/unlike`, {
            method: 'DELETE',
            mode: 'cors',
            headers: {
                'Authorization':`Bearer ${localStorage.getItem("token")}`
            }
        });
    }

    const tablesVisibility = (list, fav, bought) => {
        setIsListVisible(list);
        setIsFavVisible(fav);
        setIsBoughtVisible(bought);
    }

    useEffect( () =>{
        fetch(`http://localhost:8080/api/song/user/list/?lastShowedSong=0&countSongs=1000000`, {
            method: 'GET',
            mode:'cors',
            headers: {
                'Authorization':`Bearer ${localStorage.getItem("token")}`
            }
        })
        .then(response => {
            if(response.ok) {
                return response.json()
            }
            throw response;
        })
        .then(data => {
            console.log(data);
            setSongList(data);
        })
        .catch(error => {
            // setError(error);
        })
        .finally(() => {
            // setLoading(false);
            setFetching(false);
        })

        fetch(`http://localhost:8080/api/song/user/loved`, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization':`Bearer ${localStorage.getItem("token")}`
            }
        })
            .then(response => {
                if(response.ok) {
                    return response.json()
                }
                throw response;
            })
            .then(data => {
                console.log(data);
                setFavSongs(data);
            })
            .catch(error => {
                console.error("Error fetching favData ", error);
            })
            .finally(() => {
                setFetching(false);
            })
            fetch(`http://localhost:8080/api/song/user/bought`, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization':`Bearer ${localStorage.getItem("token")}`
            }
            })
            .then(response => {
                if(response.ok) {
                    return response.json()
                }
                throw response;
            })
            .then(data => {
                console.log(data);
                setBoughtSongs(data);
            })
            .catch(error => {
                console.error("Error fetching favData ", error);
            })
            .finally(() => {
                setFetching(false);
            })
    },[isUpdated]);

    const columnsSongList = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',

        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',

        },
        {
            title: 'Author',
            dataIndex: 'authorUsername',
            key: 'author',
        },
        {
            title: 'Votes',
            dataIndex: 'votes',
            key: 'votes',
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
        },
        {
            title: 'Buy',
            key: 'buy',
            dataIndex: 'buy',

            render: (text, record) => {
                return (
                    <>
                        { record.isBought ?
                            <></>
                            :
                            <Stripe
                                stripeKey="pk_test_51MPNmYBE524ie8PIzOsMwgEEyMlpFsJVF65bM0gbUdaqhfG4RIGOujstTtAEdVFl9UURFZGwF7rSvVB2DjMqlP1900U46wMwOl"
                                token={handleToken2}
                            />
                        }
                    </>
                );
            }
        },
        {
            title: 'Play',
            key: 'play',
            dataIndex: 'play',
            render: (text, record) => (
                <div>
                    {loading ? (
                        <Spin />
                    ) : audioUrl ? (
                        <audio controls src={record.url.json()}/>
                    ) : (
                        <Button
                            type="primary"
                            shape="circle"
                            icon={<PlayCircleOutlined/>}
                            onClick={()=>handleClickPlay(record)}
                        />
                    )}
                </div>
            ),
        },{
            key: "actions",
            title: "Actions",

            render: (text,record) => {
                return (
                    <>
                        {record.isLoved ?
                            <HeartFilled id={Math.random()} onClick={function() { handleClickLove(record) }}
                                         style={{color: "red", marginLeft: 12}}
                            />
                            :
                            <HeartOutlined id={Math.random()} onClick={function() { handleClickLove(record) }}
                                           style={{color: "red", marginLeft: 12}}
                            />
                        }
                    </>
                );
            }
        }
    ];

    const columnsFavBought = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',

        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',

        },
        {
            title: 'Author',
            dataIndex: 'author',
            key: 'author',

        },
        {
            title: 'Votes',
            dataIndex: 'votes',
            key: 'votes',

        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
        },
        {
            title: 'Play',
            key: 'play',
            dataIndex: 'play',
            render: (text, record) => (
                <div>
                    {loading ? (
                        <Spin />
                    ) : audioUrl ? (
                        <audio controls src={record.url.json()}/>
                    ) : (
                        <Button
                            type="primary"
                            shape="circle"
                            icon={<PlayCircleOutlined />}
                            onClick={()=>handleClickPlay(record)}
                        />
                    )}
                </div>
            ),
        }
    ];

    const renderListSongs = () => {
        if(!isListVisible) {
            if (fetching) {
                return <Spin indicator={antIcon}/>
            }
            if (songList.length <= 0) {

                return <>
                    <Empty/>
                    <Typography.Text style={{color: "blue", textAlign: "center", fontSize: "medium"}}>There are no new songs</Typography.Text>
                    </>
            }
        }
        return <>
            <Input
                style={{display: isListVisible ? "block" : "none"}}
                onChange={e=>setSelectedRowB(songList[parseInt(e.target.value)-1])}
                placeholder="Input a number"
                maxLength={4}
            />
        <Table
            style={{display: isListVisible ? "block" : "none"}}
            dataSource={songList}
            columns={columnsSongList}
            bordered
            title={() => 'Songs'}
            pagination={{ pageSize: 100 }}

            // scroll={{ y: 240 }}
            rowKey={(student) => student.id}
            rowSelection={{
                selectedRowKeys: [selectedRow.key],
                onChange: (s , selectedRows) => setSelectedRow(selectedRows[0])
            }}
        />
        </>;
    }

    const renderFavSongs =() => {
        if(isFavVisible) {
            if (fetching) {
                return <Spin indicator={antIcon}/>
            }
            if (favSongs.length <= 0) {
                return <Empty/>;
            }
        }
        return  <Table
            style={{display: isFavVisible ? "block" : "none" }}
            dataSource={favSongs}
            columns={columnsFavBought}
            bordered
            title={() => 'Songs'}
            pagination={{ pageSize: 50 }}
            scroll={{ y: 240 }}
            rowKey={(song) => song.id}
            rowSelection={{
                selectedRowKeys: [selectedRow.key],
                onChange: (s , selectedRows) => setSelectedRow(selectedRows[0])
            }}
        />;
    }

    const renderBoughtSongs =() => {
        if(isBoughtVisible) {
            if (fetching) {
                return <Spin indicator={antIcon}/>
            }
            if (boughtSongs.length <= 0) {
                return <Empty/>;
            }
        }
        return  <Table
            style={{display: isBoughtVisible ? "block" : "none" }}
            dataSource={boughtSongs}
            columns={columnsFavBought}
            bordered
            title={() => 'Songs'}
            pagination={{ pageSize: 50 }}
            scroll={{ y: 240 }}
            rowKey={(song) => song.id}
            rowSelection={{
                selectedRowKeys: [selectedRow.key],
                onChange: (s , selectedRows) => setSelectedRow(selectedRows[0])
            }}

        />;
    }

    return <Layout
        style={{ minHeight: '100vh', }}>
        <Sider collapsible collapsed={collapsed}
               onCollapse={setCollapsed}>
            <div
                style={{
                    height: 32,
                    margin: 16,
                    background: 'rgba(255, 255, 255, 0.2)',
                }}
            />
            <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
                <Menu.SubMenu key="sub1" title="Personal data" icon={<UserOutlined/>}>
                    <Menu.Item key="1">Id: {localStorage.getItem("id")}</Menu.Item>
                    <Menu.Item key="2">Username: {localStorage.getItem("name")}</Menu.Item>
                    <Menu.Item key="3">First name: {localStorage.getItem("firstName")}</Menu.Item>
                    <Menu.Item key="4">Last name: {localStorage.getItem("lastName")}</Menu.Item>
                    <Menu.Item key="5">Email: {localStorage.getItem("email")}</Menu.Item>
                </Menu.SubMenu>
                <Menu.Item key="6">
                    <span onClick={() => tablesVisibility(true, false, false)}>Recommended for you</span>
                </Menu.Item>
                <Menu.Item key="7">
                    <HeartFilled/>
                    <span onClick={() =>tablesVisibility(false, true, false)}>Saved songs</span>
                </Menu.Item>
                <Menu.Item key="8">
                    <BsMusicNoteBeamed/>
                    <span onClick={() =>tablesVisibility(false, false, true)}> Bought songs</span>
                </Menu.Item>
                <Button type="primary" style={{float: "center"}} onClick={() => {localStorage.clear(); navigate("/login")}}>Log out</Button>
            </Menu>
        </Sider>
        <Layout className="site-layout">
            <Header
                style={{
                    top: 0,
                }}
            >
                <Typography.Title style={{color: "white", textAlign: "center", fontSize: 30}}>MyMusic
                    <Button type="primary" style={{float: "right"}} onClick={showModal}>Creator mode</Button>
                </Typography.Title>
            </Header>

            <Modal title="Be a CREATOR" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
                <Input value={inputId} onChange={e=>setInputId(e.target.value)} placeholder="Enter your user Id"/>
            </Modal>

            <Content
                style={{
                    margin: '0 16px',
                }}
            >
                <Breadcrumb
                    style={{
                        margin: '16px 0',
                    }}
                >
                    <Breadcrumb.Item>Recommended for you</Breadcrumb.Item>
                </Breadcrumb>
                <div
                    style={{
                        padding: 24,
                        minHeight: 360,
                    }}
                >
                    {renderFavSongs()}
                    {renderListSongs()}
                    {renderBoughtSongs()}
                </div>
            </Content>
            <Footer
                style={{
                    textAlign: 'center',
                }}
            >
                By MyMusic
            </Footer>
        </Layout>
    </Layout>
}

export default UserHomeProfile;