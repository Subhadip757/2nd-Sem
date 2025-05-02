import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Helmet } from 'react-helmet';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!email || !password) {
      setError('Please enter both email and password.');
      return;
    }
    setError('');
    navigate('/'); // Redirect to home after login
  };

  return (
    <>
      <Helmet>
        <title>Login - ShopEasy</title>
      </Helmet>
      <div className="container d-flex align-items-center justify-content-center min-vh-100 bg-light">
        <div className="card shadow-sm border-0" style={{ maxWidth: 370, width: '100%' }}>
          <div className="card-body p-4">
            <h3 className="text-center text-danger fw-bold mb-4">Login to ShopEasy</h3>
            {error && <div className="alert alert-danger py-2">{error}</div>}
            <form onSubmit={handleSubmit} autoComplete="off">
  {/* Email Field */}
  <div className="mb-4">
    <label htmlFor="loginEmail" className="form-label fw-semibold">
      Email address
    </label>
    <div className="input-group">
      <span className="input-group-text bg-white text-danger px-3 border-end-0">
        <i className="fa fa-envelope"></i>
      </span>
      <input
        type="email"
        className="form-control border-start-0 px-3 py-3"
        id="loginEmail"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Enter your email"
        required
        style={{
          background: '#f9f9f9',
          borderRadius: '0 8px 8px 0',
          fontSize: '1rem',
        }}
      />
    </div>
  </div>

  {/* Password Field */}
  <div className="mb-4">
    <label htmlFor="loginPassword" className="form-label fw-semibold">
      Password
    </label>
    <div className="input-group">
      <span className="input-group-text bg-white text-danger px-3 border-end-0">
        <i className="fa fa-lock"></i>
      </span>
      <input
        type={showPassword ? 'text' : 'password'}
        className="form-control border-start-0 px-3 py-3"
        id="loginPassword"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Enter your password"
        required
        style={{
          background: '#f9f9f9',
          borderRight: 'none',
          borderRadius: '0',
          fontSize: '1rem',
        }}
      />
      <button
        type="button"
        className="btn btn-light border-start-0 px-3"
        onClick={() => setShowPassword(!showPassword)}
        tabIndex={-1}
        aria-label={showPassword ? 'Hide password' : 'Show password'}
        style={{
          borderRadius: '0 8px 8px 0',
          background: '#f9f9f9',
        }}
      >
        <i className={`fa-solid fa-eye${showPassword ? '-slash' : ''}`}></i>
      </button>
    </div>
  </div>

  {/* Submit Button */}
  <button
    type="submit"
    className="btn btn-danger w-100 py-3 fw-bold mb-2 rounded-3 shadow-sm"
  >
    Login
  </button>
</form>



            <div className="mt-2 text-center">
              <small>
                Don't have an account?{' '}
                <a href="#" className="text-danger fw-semibold text-decoration-none">
                  Sign up
                </a>
              </small>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Login;
