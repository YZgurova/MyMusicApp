import "./LoginForm.css"
import "./RegisterForm.css"
import {message, Switch} from 'antd';
import {Button, Divider, Form, Input, Typography} from "antd";
import {FacebookFilled, GoogleOutlined, TwitterOutlined} from "@ant-design/icons";
import { Link, useNavigate} from "react-router-dom";
import {useState} from "react";

function RegisterForm() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigator = useNavigate();

    const handleSubmit = (values) => {
        const data = {
            username:  values.username,
            password: values.password
        };
        // console.log(values.password);
        console.log(data);
        console.log(values);
        console.log(JSON.stringify(values));
        fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(values, null, 2)
        })
            .then((response) => response.json())
            .then((result) => {
                console.log('Success:', result);
                // redirect to another page or display a success message
            })
            .catch((error) => {
                console.error('Error:', error);
                // display an error message
            });
        setIsLoggedIn(true);
    };

    if (isLoggedIn) {
        navigator("/login");
    }

    const login = () => {
        message.success("Register successful").then();
    }
    return(
        <div className="appBg">
            <Form className="RegisterForm" onFinish={handleSubmit} >
                <Typography.Title style={{textAlign: "center"}}>Welcome</Typography.Title>
                <Form.Item rules={[
                    {
                        required: true,
                        // type: "username",

                        message: "Please enter valid first name",
                    },
                ]}
                           label="First Name"
                           name={"firstName"}>
                    <Input placeholder="First name"/>
                </Form.Item>
                <Form.Item rules={[
                    {
                        required: true,
                        // type: "username",

                        message: "Please enter valid last name",
                    },
                ]}
                           label="Last name"
                           name={"lastName"}>
                    <Input placeholder="Last name"/>
                </Form.Item>
                <Form.Item rules={[
                    {
                        required: true,
                        // type: "username",

                        message: "Please enter valid username",
                    },
                ]}
                           label="Username"
                           name={"username"}>
                    <Input placeholder="Username"/>
                </Form.Item>
                <Form.Item rules={[
                    {
                        required: true,
                        message: "Please enter your password",
                    },
                ]}label="Password" name={"password"}>
                    <Input type="password" placeholder="Password"/>
                </Form.Item>
                <Form.Item rules={[
                    {
                        required: true,
                        type: "email",
                        message: "Please enter valid email",
                    },
                ]}
                           label="Email"
                           name={"email"}>
                    <Input placeholder="Email"/>
                </Form.Item>
                <Button type="primary" htmlType="submit" block onClick={handleSubmit}>
                    Register
                </Button>
                <Typography style={{textAlign: "center"}}>If you already have an account please
                    <li><Link to="/login" className="center">Log in</Link></li>
                </Typography>
            </Form>
        </div>
    );
}
export default RegisterForm;