import "./LoginForm.css"
import React from "react";
import {message, Switch} from 'antd';
import {Button, Divider, Form, Input, Typography} from "antd";
import {FacebookFilled, GoogleOutlined, TwitterOutlined} from "@ant-design/icons";
import {
    Link,
    useNavigate
} from "react-router-dom";
import {useState} from "react";


function LoginForm() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();
    const form = document.getElementById('form');

    const handleSubmit = (values) => {
        // event.preventDefault();
        fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                    username: username,
                    password: password
                }
            )
        })
            .then(response => {
                if (response.ok) {
                    console.log("ok");
                    return response.json();
                } else {
                    // If the login was unsuccessful, show an error message
                    console.log("uh");
                    alert('Invalid username or password');
                    throw new Error('invalid username or password')
                }
            })
            .then(data => {
                debugger;
                console.log(data)
                localStorage.setItem("id", JSON.stringify(data.user.id));
                localStorage.setItem("firstName", JSON.stringify(data.user.firstName));
                localStorage.setItem("lastName", JSON.stringify(data.user.lastName));
                localStorage.setItem("name", JSON.stringify(username));
                localStorage.setItem("email", JSON.stringify(data.user.email));
                localStorage.setItem("roles", JSON.stringify(data.roles));
                localStorage.setItem("token", data.token);
                setIsLoggedIn(true);
            })
            .catch((error) => {
                 // console.error('Error:', error);
                // display an error message
            });

        };

        if (isLoggedIn) {
            return navigate("/user");
        }

        const login = () => {
            message.success("Login successful").then();
        }
        return (
            <div className="appBg">
                <Form id="form" className="loginForm" onSubmit={handleSubmit}>
                    <Typography.Title>Welcome Back</Typography.Title>
                    <Form.Item rules={[
                        {
                            required: true,
                            type: "username",
                            message: "Please enter valid username",
                        },
                    ]}
                               label="Username"
                               name={"username"}>
                        <Input onChange={(e) => setUsername(e.target.value)} placeholder="Enter your username"/>
                    </Form.Item>
                    <Form.Item rules={[
                        {
                            required: true,
                            message: "Please enter your password",
                        },
                    ]} label="Password" name={"password"}>
                        <Input type="password" onChange={(e) => setPassword(e.target.value)} placeholder="Enter your password"/>
                    </Form.Item>
                    <Button type="primary" htmlType="submit" block onClick={handleSubmit}>
                        Login
                    </Button>
                    <li><Link to="/register">Register</Link></li>
                </Form>
            </div>
        );
}
export default LoginForm;