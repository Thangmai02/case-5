let display = document.getElementById("display");
showLogin()
showRegister()
function showLogin() {
    let str =`
    <h1> Login page</h1>
    <input type="text" id="usn" placeholder="nhap User name">
    <input type="text" id="pass" placeholder="nhap Password">
    <button onclick="login()" > login</button>
    <p onclick="showRegister()"> Register</p>
    `
 display.innerHTML= str
}
function showRegister() {
    let str =`
<h1> register page</h1>
    <input type="text" id="usn" placeholder="nhap User name">
    <input type="text" id="pass" placeholder="nhap Password">
    <input type="text" id="dob" placeholder="nhap dob">
    <button > Register</button>
    <p onclick="showLogin()"> Login</p>
    `
    display.innerHTML= str
}
function login() {
    let usn = document.getElementById('usn').value;
    let pass = document.getElementById('pass').value;
    let data = { usn,pass};
    axios.post('http://localhost:8080/login',data).then((res)=>{
        console.log(res)
        if ( res.data==="Tài khoản hoặc mật khẩu sai !"){
            alert(res.data);
        }else {
            showHome()
            alert(" ok")
        }
    })
}
function showHome() {
    let str =`
    <h1> home page</h1>
    <button onclick="showLogin()"> logout</button>
    
    `
   display.innerHTML=str
}