const API_URL = "http://localhost:8080/api/account";


function login(event) {

    event.preventDefault();

    const username =
        document.getElementById("username").value;

    const password =
        document.getElementById("password").value;

    if (username === "admin" && password === "admin123") {

        window.location.href = "dashboard.html";

    } else {

        document.getElementById("message").innerText =
            "Invalid username or password";
    }
}


function loadAccount() {

    fetch(API_URL)
        .then(response => response.json())
        .then(account => {

            document.getElementById("customerName")
                .innerText = account.customerName;

            document.getElementById("accountNumber")
                .innerText = account.accountNumber;

            document.getElementById("balance")
                .innerText = account.balance.toFixed(2);

        })
        .catch(error => {

            console.error(error);

            document.getElementById("message")
                .innerText = "Unable to connect to server";

        });
}


function deposit() {

    const amount =
        document.getElementById("depositAmount").value;

    fetch(`${API_URL}/deposit?amount=${amount}`, {
        method: "POST"
    })
        .then(response => response.json())
        .then(account => {

            document.getElementById("balance")
                .innerText = account.balance.toFixed(2);

            document.getElementById("message")
                .innerText = "Deposit successful";

        });
}


function withdraw() {

    const amount =
        document.getElementById("withdrawAmount").value;

    fetch(`${API_URL}/withdraw?amount=${amount}`, {
        method: "POST"
    })
        .then(response => response.json())
        .then(account => {

            document.getElementById("balance")
                .innerText = account.balance.toFixed(2);

            document.getElementById("message")
                .innerText = "Withdrawal successful";

        });
}


function logout() {

    window.location.href = "index.html";

}


if (window.location.pathname.includes("dashboard.html")) {

    loadAccount();

}
