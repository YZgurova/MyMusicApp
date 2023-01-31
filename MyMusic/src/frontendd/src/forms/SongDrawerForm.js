import {Drawer, Input, Col, Select, Form, Row, Button, Upload, message} from 'antd'
import React, {useState} from "react";
import CurrencyInput from 'react-currency-input-field';
import {LoadingOutlined, PlusOutlined} from "@ant-design/icons";
import {useNavigate} from "react-router-dom";

const {Option} = Select;

function SongDrawerForm({showDrawer, setShowDrawer}) {
    const [file, setFile] = useState(null);
    const [fileName, setFileName] = useState('Choose File');
    const [price, setPrice] = useState(0);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const onClose=()=>setShowDrawer(false);
    console.log(showDrawer);

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

    // const handleSubmit = () => {
    //     setLoading(true);
    //     const formData = new FormData();
    //     formData.append('file', file);
    //
    //     axios.post('/api/upload', formData, {
    //         headers: {
    //             'Content-Type': 'multipart/form-data'
    //         }
    //     }).then((response) => {
    //         console.log(response.data);
    //         message.success('File uploaded successfully');
    //         setLoading(false);
    //     }).catch((error) => {
    //         console.log(error);
    //         message.error('There was an error uploading the file');
    //         setLoading(false);
    //     });
    // };

    const uploadButton = (
        <div>
            {loading ? <LoadingOutlined /> : <PlusOutlined />}
            <div className="ant-upload-text">{fileName}</div>
        </div>
    );

    const saveSong = async () => {
        debugger;
        const formData = new FormData();
        formData.append('file', file);
        const a = await (await fetch("http://localhost:8080/api/auth/storage", {
            method: 'POST',
            mode: 'cors',
            header: {
                'Content-Type': 'multipart/form-data'
            },
            body: formData
        })).text();

        return await fetch("http://localhost:8080/api/creator", {
                method: 'POST',
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization':`Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify({
                    name: fileName,
                    url: a,
                    price: price
                })
            });
    };

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
                    <Form.Item
                        name="price"
                        label="price"
                        rules={[{required: true, message: 'Please enter'}]}>
                        <CurrencyInput onChange={(e) => setPrice(e.target.value)} placeholder="Please enter song price" />
                    </Form.Item>
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
export default SongDrawerForm;