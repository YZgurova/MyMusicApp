import React, { useState, useEffect } from 'react'
import {listSong, listUsers} from "./client";
// import {AddSongForm} from "./forms/AddSongForm"
import SongDrawerForm from "./forms/SongDrawerForm"
import {
    Breadcrumb, Button,
    Empty, Input,
    Layout,
    Menu, message, Modal,
    Spin,
    Table,
    Typography, Upload
} from 'antd';
import {
    DeleteOutlined,
    DesktopOutlined, EditOutlined,
    FileOutlined,
    LoadingOutlined,
    PieChartOutlined, PlayCircleOutlined, PlusOutlined,
} from '@ant-design/icons';
import Search from "antd/es/input/Search";
import Title from "antd/es/skeleton/Title";
import axios from "axios";
import AWS from "aws-sdk";
import fetch from "unfetch";
// import './App.css';

const {Header, Content, Footer, Sider} =Layout;

const S3_BUCKET = 'mymusicapplication';
const FILE_NAME = 'Queen.mp3';

function AdminProfile() {

    const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;
    const onSearch = (value: string) => console.log(value);

    const [isEditing, setIsEditing] = useState(false);
    const [editingSong, setEditingSong] = useState(null);

    const[creator, setCreator] = useState([]);
    const[song, setSong] = useState([]);
    const[collapsed, setCollapsed] = useState(false);
    const[fetching, setFetching] = useState(true);

    const [showDrawer, setShowDrawer] = useState(false);

    const [loading, setLoading] = React.useState(false);
    const [audioUrl, setAudioUrl] = React.useState(null);

    const columns = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',

        },
        {
            title: 'First name',
            dataIndex: 'firstName',
            key: 'firstName',

        },
        {
            title: 'Last name',
            dataIndex: 'lastName',
            key: 'lastName',
        },
        {
            title: 'Username',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            key: "actions",
            title: "Actions",
            render: (record) => {
                return (
                    <DeleteOutlined
                        onClick={() => {
                            onDeleteSong(record);
                        }}
                        style={{color: "red", marginLeft: 12}}
                    />
                );
            }
        }
    ];


    const onAddSong = () => {
        const randomNumber = parseInt(Math.random() * 1000);
        const newStudent = {
            id: randomNumber,
            name: "Name " + randomNumber,
            email: randomNumber + "@gmail.com",
            address: "Address " + randomNumber,
        };
        setSong((pre) => {
            return [...pre, newStudent];
        });
    };
    const onDeleteSong = (record) => {
        Modal.confirm({
            title: "Are you sure, you want to delete this song record?",
            okText: "Yes",
            okType: "danger",
            onOk: () => {
                setSong((pre) => {
                    return fetch("http://localhost:8080/api/song/{song.id}")
                        .then(pre.filter((song) => song.id !== record.id));
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
        fetch('http://localhost:8080/api/song/creator/1')
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
    },[])

    function handlePlayClick() {
        setLoading(true);
        const s3 = new AWS.S3({
            accessKeyId: process.env.REACT_APP_AWS_ACCESS_KEY_ID,
            secretAccessKey: process.env.REACT_APP_AWS_SECRET_ACCESS_KEY,
        });
        const params = {
            Bucket: S3_BUCKET,
            Key: FILE_NAME,
        };
        s3.getObject(params, (error, data) => {
            setLoading(false);
            if (error) {
                message.error(error.message).then( );
            } else {
                const audioBlob = new Blob([data.Body], {
                    type: 'audio/mpeg',
                });
                setAudioUrl(URL.createObjectURL(audioBlob));
            }
        });
    }

    const renderSongs = () => {
        if(fetching) {
            return <Spin indicator={antIcon} />
        }
        if(song.length<=0) {
            return <Empty />;
        }
        return <>
            <SongDrawerForm
                showDrawer={showDrawer}
                setShowDrawer={showDrawer}
            />
            {/*<Table*/}
            {/*    dataSource={song}*/}
            {/*    columns={columns}*/}
            {/*    bordered*/}
            {/*    title = {() =>*/}
            {/*        <Button*/}
            {/*            onClick={() => setShowDrawer(!showDrawer)}*/}
            {/*            type="primary" shape="round" icon={<PlusOutlined/>} size="small">*/}
            {/*            Add new song*/}
            {/*        </Button>}*/}
            {/*    pagination={{ pageSize: 50 }}*/}
            {/*    scroll={{ y: 240 }}*/}
            {/*    rowKey={(song) => song.id}/>*/}
            <div>
                <header>
                    <Button
                        onClick={() => setShowDrawer(!showDrawer)}
                        type="primary" shape="round" icon={<PlusOutlined/>} size="small">
                        Add a new song
                    </Button>
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
                        <Input
                            value={editingSong?.email}
                            onChange={(e) => {
                                setEditingSong((pre) => {
                                    return { ...pre, email: e.target.value };
                                });
                            }}
                        />
                        <Input
                            value={editingSong?.address}
                            onChange={(e) => {
                                setEditingSong((pre) => {
                                    return { ...pre, address: e.target.value };
                                });
                            }}
                        />
                    </Modal>
                </header>
            </div>
        </>
    }

    function getItem(label, key, icon, children) {
        return {
            key,
            icon,
            children,
            label,
        };
    }
    const items = [
        getItem('Albums', '1', <PieChartOutlined />),
        getItem('Songs', '2', <DesktopOutlined />),
        getItem('Subscribers', '3', <FileOutlined />),
    ];

    return <Layout
        style={{ minHeight: '100vh', }}>
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
            <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline" items={items}/>
        </Sider>
        <Layout className="site-layout">
            <Header
                style={{
                    padding: 0,
                    //background: colorBgContainer,
                }}
            >
                <Typography.Title style={{color: "white", textAlign: "center", fontSize: "larger"}}>MyMusic
                    <Button type="primary" style={{float: "right"}}>User mode</Button>
                </Typography.Title>
            </Header>

            <Header
                style={{
                    padding: 0,
                }}
            >
                <Search placeholder="input search text" onSearch={onSearch} enterButton />
            </Header>
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
                    <Breadcrumb.Item>Users</Breadcrumb.Item>
                </Breadcrumb>

                <div
                    style={{
                        padding: 24,
                        minHeight: 360,
                    }}
                >
                    {renderSongs()}
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

export default AdminProfile;