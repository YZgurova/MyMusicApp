import React, { useState, useEffect } from 'react'
import SongDrawerForm from "./forms/SongDrawerForm"
import {
    Breadcrumb, Button,
    Empty, Input,
    Layout,
    Menu, Modal,
    Spin,
    Table,
    Typography
} from 'antd';
import {
    DeleteOutlined,
    EditOutlined, HeartFilled,
    LoadingOutlined,
    PlayCircleOutlined, PlusOutlined, UserOutlined, WalletFilled,
} from '@ant-design/icons';
import fetch from "unfetch";
import {useNavigate} from "react-router-dom";
import {BsMusicNoteBeamed} from "react-icons/bs";

const {Header, Content, Footer, Sider} =Layout;

function CreatorProfile() {

    const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

    const [isEditing, setIsEditing] = useState(false);
    const [editingSong, setEditingSong] = useState(null);

    const[song, setSong] = useState([]);
    const[wallet, setWallet] = useState(0.0);
    const[collapsed, setCollapsed] = useState(false);
    const[fetching, setFetching] = useState(true);

    const [showDrawer, setShowDrawer] = useState(false);

    const [selectedRow, setSelectedRow] = useState({});
    const [isPlaying, setIsPlaying] = useState(false);
    const [loading, setLoading] = React.useState(false);
    const [audioUrl, setAudioUrl] = React.useState(null);
    const navigate = useNavigate();
    const urlsMap = new Map();
    const handle = newValue => {setShowDrawer(newValue)};

    const getWallet = async () =>{
        const amount = await (await fetch("http://localhost:8080/api/wallet", {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        })).json();
        setWallet(amount.resources);

    }

    function handleClickPlay(record){
        console.log(record);
        setSelectedRow(record);
        setIsPlaying(urlsMap.set(record.id, !urlsMap.get(record.id)));
        handleAudioPlay(selectedRow.url, isPlaying);
    }

    const handleAudioPlay = (s3_url_link, state) => {
        debugger;
        console.log(s3_url_link);
        var audio = new Audio(s3_url_link);
        audio.pause();
        console.log(state.value);
        if (!state.value) {
            audio.currentTime=8;
            audio.play();
        } else {
            audio.pause();
        }
    };

    const columns = [
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
                        <audio controls src={record.url} />
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
        },
        {
            key: "actions",
            title: "Actions",
            render: (record) => {
                return (
                    <>
                        <DeleteOutlined
                            onClick={() => {
                                onDeleteSong(record);
                            }}
                            style={{color: "red", marginLeft: 12}}
                        />
                    </>
                );
            }
        }
    ];

    const onDeleteSong = (record) => {
        Modal.confirm({
            title: "Are you sure, you want to delete this song record?",
            okText: "Yes",
            okType: "danger",
            onOk: () => {
                console.log("id: " + record.id);
                fetch(`http://localhost:8080/api/song/${record.id}`, {
                    method: 'DELETE',
                    mode: 'cors',
                    headers: {
                        'Authorization':`Bearer ${localStorage.getItem("token")}`
                    }
                }).then(() => {
                    setSong(song.filter(s => s.id !== record.id));
                });
            },
        });
    };

    const onEditSong = (record) => {
        setIsEditing(true);
        setEditingSong({ ...record });
    };
    const resetEditing = () => {
        setIsEditing(false);
        setEditingSong(null);
    };

    useEffect( () =>{
        debugger;
        fetch(`http://localhost:8080/api/creator/list/songs/?lastShowedSong=0&countSongs=100000`, {
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
            setSong(data);
        })
        .catch(error => {
            console.error("Error fetching data ", error);
            // setError(error);
        })
        .finally(() => {
            setFetching(false);
        })
    },[showDrawer])

    useEffect(() => {
        getWallet()
        }, [])

    const addSongButton =() => {
        return <div>
            <header>
                <SongDrawerForm
                    showDrawer={showDrawer}
                    setShowDrawer={handle}
                />
                <Button
                    onClick={() => setShowDrawer(!showDrawer)}
                    type="primary" shape="round" icon={<PlusOutlined/>} size="small">
                    Add a new song
                </Button>
            </header>
        </div>
    }

    const renderSongs = () => {
        if(fetching) {
            return <Spin indicator={antIcon} />
        }
        if(song.length<=0) {
            return <Empty />;
        }
        return <>
            <div>
                <header>
                    <Table columns={columns} dataSource={song}></Table>
                    <Modal
                        title="Edit Song"
                        visible={isEditing}
                        okText="Save"
                        onCancel={() => {
                            resetEditing();
                        }}
                        onOk={() => {
                            setSong((pre) => {
                                return pre.map((student) => {
                                    if (song.id === editingSong.id) {
                                        return editingSong;
                                    } else {
                                        return song;
                                    }
                                });
                            });
                            resetEditing();
                        }}
                    >
                        <Input
                            value={editingSong?.name}
                            onChange={(e) => {
                                setEditingSong((pre) => {
                                    return { ...pre, name: e.target.value };
                                });
                            }}
                        />
                    </Modal>
                </header>
            </div>
        </>
    }

    const renderPage = () =>{
        navigate("/user");
    }

    return <Layout style={{ minHeight: '100vh', }}>
        <Sider collapsible collapsed={collapsed}
                onCollapse={setCollapsed}
        >
            <div
                style={{
                    height: 32,
                    margin: 16,
                    background: 'rgba(255, 255, 255, 0.2)',
                }}
            />
            <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
                <Menu.Item key="2">
                    <BsMusicNoteBeamed />
                    <span>Songs</span>
                </Menu.Item>
                <Menu.Item key="3">
                    <WalletFilled />
                    <span>Wallet: {wallet}$</span>
                </Menu.Item>
            </Menu>
        </Sider>
        <Layout className="site-layout">
            <Header style={{ padding: 0}}>
                <Typography.Title style={{color: "white", textAlign: "center", fontSize: "larger"}}>MyMusic
                    <Button type="primary" style={{float: "right"}} onClick={renderPage}>User mode</Button>
                </Typography.Title>
            </Header>
            <Content style={{margin: '0 16px'}}>
                <Breadcrumb style={{margin: '16px 0'}}>
                    <Breadcrumb.Item>Your creator profile</Breadcrumb.Item>
                </Breadcrumb>
                <div style={{padding: 24,minHeight: 360}}>
                    {addSongButton()}
                    {renderSongs()}
                </div>
            </Content>
            <Footer style={{textAlign: 'center'}}>
                By MyMusic
            </Footer>
        </Layout>
    </Layout>
}

export default CreatorProfile;