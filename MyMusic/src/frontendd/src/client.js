// import fetch from 'unfetch';
//
// const checkStatus = response => {
//     if (response.ok) {
//         return response;
//     } else {
//         let error = new Error(response.statusText);
//         error.response = response;
//         response.json().then(e => {
//             error.error = e;
//         });
//         return Promise.reject(error);
//     }
// }
//
// export const getAllStudents = () =>
//     fetch('api/students').then(checkStatus);
//
// export const addNewStudent = student =>
//     fetch('api/students', {
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         method: 'POST',
//         body: JSON.stringify(student)
//     })
//         .then(checkStatus);
//
// export const updateStudent = (studentId, student) =>
//     fetch(`api/students/${studentId}`, {
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         method: 'PUT',
//         body: JSON.stringify(student)
//     })
//         .then(checkStatus);
//
// export const deleteStudent = studentId =>
//     fetch(`api/students/${studentId}`, {
//         method: 'DELETE'
//     })
//         .then(checkStatus);

import fetch from 'unfetch';
//here we will write all function request to backend
const checkStatus = response => {
    if(response.ok) {
        return response;
    }

    const error = new Error(response.statusText);
    error.response = response;
    return Promise.reject(error)
}
export const listUsers = () =>
    fetch("http://localhost:8080/api/user")
        .then(checkStatus);

export const addSong = () =>
    fetch("http://localhost:8080/creator/api/song")
        .then(checkStatus);
export const addSongToS3 =() =>
    fetch("http://localhost:8080/api/storage/uploadFile")
        .then(checkStatus);

export const listSong = () =>
    fetch("http://localhost:8080/creator/api/song/1")
        .then(checkStatus)

