import { useState } from "react";

function Login({ onLogin }) {
  const [isRegistering, setIsRegistering] = useState(false);

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        "http://localhost:8080/api/auth/login",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email,
            password,
          }),
        }
      );

      const data = await response.json();

      if (response.ok) {
        localStorage.setItem("token", data.token);
        onLogin();
      } else {
        alert(data.message || "Login failed");
      }
    } catch (error) {
      alert("Could not connect to the server");
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        "http://localhost:8080/api/auth/register",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            fullName,
            email,
            password,
          }),
        }
      );

      const data = await response.json();

      if (response.ok) {
        alert("Account created successfully! Please login.");

        setFullName("");
        setEmail("");
        setPassword("");

        setIsRegistering(false);
      } else {
        alert(data.message || "Registration failed");
      }
    } catch (error) {
      alert("Could not connect to the server");
    }
  };

  const switchMode = () => {
    setIsRegistering(!isRegistering);

    setFullName("");
    setEmail("");
    setPassword("");
  };

  return (
    <div className="login-page">

      {/* Background decoration */}

      <div className="background-circle circle-one"></div>
      <div className="background-circle circle-two"></div>


      {/* Left side */}

      <div className="intro-section">

        <div className="wallet-icon">
          ₹
        </div>

        <h1>
          Expense
          <br />
          Tracker
        </h1>

        <div className="small-line"></div>

        <p>
          Track your expenses.
          <br />
          Take control of your money.
        </p>

        <div className="finance-icons">
          <span>₹</span>
          <span>↗</span>
          <span>▥</span>
        </div>

      </div>


      {/* Login / Register card */}

      <div className="login-box">

        <div className="login-icon">
          ₹
        </div>

        <h2>
          Expense Tracker
        </h2>

        <p className="login-title">
          {isRegistering
            ? "Create your account"
            : "Login to your account"}
        </p>


        {isRegistering ? (

          /* REGISTER FORM */

          <form onSubmit={handleRegister}>

            {/* Full Name */}

            <div className="input-group">

              <span>👤</span>

              <input
                type="text"
                placeholder="Full Name"
                value={fullName}
                onChange={(e) =>
                  setFullName(e.target.value)
                }
                required
              />

            </div>


            {/* Email */}

            <div className="input-group">

              <span>✉</span>

              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) =>
                  setEmail(e.target.value)
                }
                required
              />

            </div>


            {/* Password */}

            <div className="input-group">

              <span>🔒</span>

              <input
                type="password"
                placeholder="Password (minimum 6 characters)"
                value={password}
                onChange={(e) =>
                  setPassword(e.target.value)
                }
                minLength="6"
                required
              />

            </div>


            <button type="submit">
              Create Account
            </button>

          </form>

        ) : (

          /* LOGIN FORM */

          <form onSubmit={handleLogin}>

            {/* Email */}

            <div className="input-group">

              <span>✉</span>

              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) =>
                  setEmail(e.target.value)
                }
                required
              />

            </div>


            {/* Password */}

            <div className="input-group">

              <span>🔒</span>

              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) =>
                  setPassword(e.target.value)
                }
                required
              />

            </div>


            <button type="submit">
              Login
            </button>

          </form>

        )}


        {/* Switch Login / Register */}

        <div className="switch-auth">

          {isRegistering ? (
            <p>
              Already have an account?{" "}
              <button
                type="button"
                onClick={switchMode}
              >
                Login
              </button>
            </p>
          ) : (
            <p>
              Don't have an account?{" "}
              <button
                type="button"
                onClick={switchMode}
              >
                Create Account
              </button>
            </p>
          )}

        </div>

      </div>

    </div>
  );
}

export default Login;