import {Drawer, Input, Col, Select, Form, Row, Button, Upload, message} from 'antd'
import React, {useEffect, useState} from "react";
import axios, {toFormData} from "axios";
import {LoadingOutlined, PlusOutlined} from "@ant-design/icons";
import {useNavigate} from "react-router-dom";

const {Option} = Select;

function AddCreatorDrawer({showDrawer, setShowDrawer}) {
    const [file, setFile] = useState(null);
    const [fileName, setFileName] = useState('Choose File');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const onClose=()=>setShowDrawer(false);

    const onFinish = values => {
        alert(JSON.stringify(values, null, 2));
    };

    const  onFinishFailed = errorInfo => {
        alert(JSON.stringify(errorInfo, null, 2));
    };

    const handleChange = (info) => {
        setFile(info.file.originFileObj);
        setFileName(info.file.name);
    };

    const handleSubmit = () => {
        setLoading(true);
        const formData = new FormData();
        formData.append('file', file);

        axios.post('/api/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).then((response) => {
            console.log(response.data);
            message.success('File uploaded successfully');
            setLoading(false);
        }).catch((error) => {
            console.log(error);
            message.error('There was an error uploading the file');
            setLoading(false);
        });
    };

    const uploadButton = (
        <div>
            {loading ? <LoadingOutlined /> : <PlusOutlined />}
            <div className="ant-upload-text">{fileName}</div>
        </div>
    );

    const saveSong = async () => {
        const formData = new FormData();
        formData.append('file', file);
        const a = await (await fetch("http://localhost:8080/api/storage/uploadFile", {
            method: 'POST',
            mode: 'cors',
            header: {
                'Content-Type': 'multipart/form-data'
            },
            body: formData
        })).text();

        const res = await fetch("http://localhost:8080/api/song", {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: fileName,
                url: a,
                authorId:2,
                album: "a"
            })
        });
    };

    console.log(showDrawer);
    return <Drawer
        title="Create new song"
        width={720}
        onClose={onClose}
        visible={showDrawer}
        bodyStyle={{paddingBottom: 80}}
        footer={
            <div
                style={{ textAlign:'right'
                }}
            >
                <Button onClick={onClose} style={{marginRight: 8}}> Cansel</Button>
            </div>
        }
    >
        <Form layout="vertical"
              onFinishFailed={onFinishFailed}
              onFinish={onFinish}
              hideRequiredMark>
            <Row gutter={16}>
                <Col span={12}>
                    <Form.Item
                        name="name"
                        label="Name"
                        rules={[{required: true, message: 'Please enter'}]}>
                        <Input placeholder="Please enter song name"/>
                    </Form.Item>
                </Col>
            </Row>
            <Row>
                <Col>
                    <Form.Item>
                        <Upload
                            name="file"
                            listType="text"
                            type="file"
                            className="upload-demo-start"
                            accept=".mp3"
                            fileList={file ? [{ name: fileName }] : []}
                            onChange={handleChange}
                        >
                            {file ? null : uploadButton}
                        </Upload>
                    </Form.Item>
                </Col>
            </Row>
            <Row>
                <Col>
                    <Form.Item>
                        <Button type="primary" htmlType="submit" onClick={saveSong}>
                            Submit
                        </Button>
                    </Form.Item>
                </Col>
            </Row>
        </Form>
    </Drawer>
}
export default AddCreatorDrawer;