import React, { useState, useRef, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import { Link } from 'react-router-dom';
import CartCount from '../Components/CartCount';

function Profile() {
  const [activeTab, setActiveTab] = useState('profile');
  const [isEditing, setIsEditing] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const fileInputRef = useRef(null);
  
  const [user, setUser] = useState({
    name: 'John Doe',
    email: 'john.doe@example.com',
    phone: '+1 234 567 8900',
    address: '123 Main St, City, Country',
    joinDate: 'January 2024',
    avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80',
    preferences: {
      notifications: true,
      darkMode: false,
      language: 'English'
    }
  });

  const [stats, setStats] = useState({
    orders: 15,
    wishlist: 8,
    reviews: 12,
    points: 2500
  });

  const [orders, setOrders] = useState([]);

  useEffect(() => {
    loadOrders();
    // Update stats based on orders
    const lastOrder = JSON.parse(localStorage.getItem('lastOrder'));
    const checkoutCart = JSON.parse(localStorage.getItem('checkoutCart'));
    
    if (lastOrder && checkoutCart) {
      setStats(prevStats => ({
        ...prevStats,
        orders: prevStats.orders + 1,
        points: prevStats.points + Math.floor(checkoutCart.summary.total)
      }));
    }
  }, []);

  const loadOrders = () => {
    try {
      const lastOrder = JSON.parse(localStorage.getItem('lastOrder'));
      const checkoutCart = JSON.parse(localStorage.getItem('checkoutCart'));
      
      if (lastOrder && checkoutCart) {
        const orderItems = checkoutCart.items.map(item => ({
          name: item.title,
          image: item.thumbnail
        }));

        const newOrder = {
          id: Date.now(),
          date: new Date().toLocaleDateString(),
          total: `$${checkoutCart.summary.total.toFixed(2)}`,
          status: 'Processing',
          items: orderItems
        };

        setOrders(prevOrders => [newOrder, ...prevOrders]);
      }
    } catch (error) {
      console.error('Error loading orders:', error);
    }
  };

  const [selectedOrder, setSelectedOrder] = useState(null);

  const handleEditProfile = () => {
    setIsEditing(true);
  };

  const handleSaveProfile = () => {
    setIsEditing(false);
    updateStats({
      orders: stats.orders + 1,
      points: stats.points + 100
    });
    alert('Profile updated successfully!');
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setUser(prevUser => ({ ...prevUser }));
  };

  const handlePreferenceChange = (key, value) => {
    setUser(prevUser => ({
      ...prevUser,
      preferences: {
        ...prevUser.preferences,
        [key]: value
      }
    }));
  };

  const handleAvatarClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setIsUploading(true);
      // Simulate file upload
      setTimeout(() => {
        const reader = new FileReader();
        reader.onloadend = () => {
          setUser(prev => ({ ...prev, avatar: reader.result }));
          setIsUploading(false);
        };
        reader.readAsDataURL(file);
      }, 1000);
    }
  };

  const updateStats = (newStats) => {
    setStats(prevStats => ({ ...prevStats, ...newStats }));
  };

  const renderProfileContent = () => (
    <div className="card glass-morphism border-0">
      <div className="card-body">
        <h4 className="text-white mb-4">Account Information</h4>
        <div className="text-center mb-4">
          <div className="position-relative d-inline-block">
            <img
              src={user.avatar}
              alt="Profile"
              className="rounded-circle mb-3"
              style={{ 
                width: '150px', 
                height: '150px', 
                objectFit: 'cover',
                cursor: 'pointer',
                border: '3px solid rgba(255,255,255,0.2)'
              }}
              onClick={handleAvatarClick}
            />
            {isUploading && (
              <div className="position-absolute top-50 start-50 translate-middle">
                <div className="spinner-border text-light" role="status">
                  <span className="visually-hidden">Loading...</span>
                </div>
              </div>
            )}
            <input
              type="file"
              ref={fileInputRef}
              onChange={handleFileChange}
              accept="image/*"
              className="d-none"
            />
            <div className="position-absolute bottom-0 end-0">
              <button 
                className="btn btn-primary btn-sm rounded-circle"
                onClick={handleAvatarClick}
              >
                <i className="fas fa-camera"></i>
              </button>
            </div>
          </div>
        </div>
        <div className="row">
          <div className="col-md-6 mb-3">
            <label className="form-label text-white-50">Full Name</label>
            <input
              type="text"
              className="form-control glass-morphism"
              value={user.name}
              onChange={(e) => setUser(prev => ({ ...prev, name: e.target.value }))}
              readOnly={!isEditing}
            />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label text-white-50">Email</label>
            <input
              type="email"
              className="form-control glass-morphism"
              value={user.email}
              onChange={(e) => setUser(prev => ({ ...prev, email: e.target.value }))}
              readOnly={!isEditing}
            />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label text-white-50">Phone</label>
            <input
              type="tel"
              className="form-control glass-morphism"
              value={user.phone}
              onChange={(e) => setUser(prev => ({ ...prev, phone: e.target.value }))}
              readOnly={!isEditing}
            />
          </div>
          <div className="col-md-6 mb-3">
            <label className="form-label text-white-50">Address</label>
            <input
              type="text"
              className="form-control glass-morphism"
              value={user.address}
              onChange={(e) => setUser(prev => ({ ...prev, address: e.target.value }))}
              readOnly={!isEditing}
            />
          </div>
        </div>
        <div className="mt-4">
          {!isEditing ? (
            <button className="btn btn-primary me-2" onClick={handleEditProfile}>
              <i className="fas fa-edit me-2"></i>Edit Profile
            </button>
          ) : (
            <>
              <button className="btn btn-success me-2" onClick={handleSaveProfile}>
                <i className="fas fa-save me-2"></i>Save Changes
              </button>
              <button className="btn btn-outline-danger" onClick={handleCancelEdit}>
                <i className="fas fa-times me-2"></i>Cancel
              </button>
            </>
          )}
          <button className="btn btn-outline-danger ms-2">
            <i className="fas fa-key me-2"></i>Change Password
          </button>
        </div>
      </div>
    </div>
  );

  const renderOrdersContent = () => (
    <div className="card glass-morphism border-0">
      <div className="card-body">
        <h4 className="text-white mb-4">Order History</h4>
        {orders.length === 0 ? (
          <div className="text-center py-5">
            <i className="fas fa-shopping-bag fa-3x text-white-50 mb-3"></i>
            <h5 className="text-white">No orders yet</h5>
            <p className="text-white-50">Your order history will appear here</p>
          </div>
        ) : (
          <div className="table-responsive">
            <table className="table table-dark table-hover">
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Date</th>
                  <th>Items</th>
                  <th>Total</th>
                  <th>Status</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {orders.map(order => (
                  <tr key={order.id}>
                    <td>#{order.id}</td>
                    <td>{order.date}</td>
                    <td>
                      <div className="d-flex align-items-center">
                        {order.items.map((item, index) => (
                          <img
                            key={index}
                            src={item.image}
                            alt={item.name}
                            className="rounded me-2"
                            style={{ width: '40px', height: '40px', objectFit: 'cover' }}
                            title={item.name}
                          />
                        ))}
                      </div>
                    </td>
                    <td>{order.total}</td>
                    <td>
                      <span className={`badge bg-${order.status === 'Delivered' ? 'success' : 'warning'}`}>
                        {order.status}
                      </span>
                    </td>
                    <td>
                      <button 
                        className="btn btn-sm btn-outline-primary"
                        onClick={() => setSelectedOrder(order)}
                      >
                        <i className="fas fa-eye me-1"></i>View
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Order Details Modal */}
        {selectedOrder && (
          <div className="modal fade show" style={{ display: 'block' }} tabIndex="-1">
            <div className="modal-dialog modal-dialog-centered">
              <div className="modal-content bg-dark text-white">
                <div className="modal-header border-secondary">
                  <h5 className="modal-title">Order #{selectedOrder.id}</h5>
                  <button 
                    type="button" 
                    className="btn-close btn-close-white"
                    onClick={() => setSelectedOrder(null)}
                  ></button>
                </div>
                <div className="modal-body">
                  <div className="mb-3">
                    <strong>Date:</strong> {selectedOrder.date}
                  </div>
                  <div className="mb-3">
                    <strong>Status:</strong>{' '}
                    <span className={`badge bg-${selectedOrder.status === 'Delivered' ? 'success' : 'warning'}`}>
                      {selectedOrder.status}
                    </span>
                  </div>
                  <div className="mb-3">
                    <strong>Items:</strong>
                    <div className="mt-2">
                      {selectedOrder.items.map((item, index) => (
                        <div key={index} className="d-flex align-items-center mb-2">
                          <img
                            src={item.image}
                            alt={item.name}
                            className="rounded me-2"
                            style={{ width: '50px', height: '50px', objectFit: 'cover' }}
                          />
                          <span>{item.name}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                  <div className="mb-3">
                    <strong>Total:</strong> {selectedOrder.total}
                  </div>
                </div>
                <div className="modal-footer border-secondary">
                  <button 
                    type="button" 
                    className="btn btn-secondary"
                    onClick={() => setSelectedOrder(null)}
                  >
                    Close
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );

  const renderSettingsContent = () => (
    <div className="card glass-morphism border-0">
      <div className="card-body">
        <h4 className="text-white mb-4">Preferences</h4>
        <div className="form-check form-switch mb-3">
          <input
            className="form-check-input"
            type="checkbox"
            checked={user.preferences.notifications}
            onChange={(e) => handlePreferenceChange('notifications', e.target.checked)}
          />
          <label className="form-check-label text-white">Email Notifications</label>
        </div>
        <div className="form-check form-switch mb-3">
          <input
            className="form-check-input"
            type="checkbox"
            checked={user.preferences.darkMode}
            onChange={(e) => handlePreferenceChange('darkMode', e.target.checked)}
          />
          <label className="form-check-label text-white">Dark Mode</label>
        </div>
        <div className="mb-3">
          <label className="form-label text-white-50">Language</label>
          <select 
            className="form-select glass-morphism" 
            value={user.preferences.language}
            onChange={(e) => handlePreferenceChange('language', e.target.value)}
          >
            <option value="English">English</option>
            <option value="Spanish">Spanish</option>
            <option value="French">French</option>
          </select>
        </div>
        <button 
          className="btn btn-primary"
          onClick={() => {
            alert('Preferences saved!');
            // Here you would typically make an API call to save preferences
          }}
        >
          <i className="fas fa-save me-2"></i>Save Preferences
        </button>
      </div>
    </div>
  );

  const renderStatsCards = () => (
    <div className="row mb-4">
      <div className="col-md-3">
        <div className="card glass-morphism border-0 h-100">
          <div className="card-body text-center">
            <div className="stat-icon">
              <i className="fas fa-shopping-bag"></i>
            </div>
            <h3 className="stat-value">{stats.orders}</h3>
            <p className="stat-label">Total Orders</p>
            <small className="text-muted d-block">Last order: {new Date().toLocaleDateString()}</small>
          </div>
        </div>
      </div>
      <div className="col-md-3">
        <div className="card glass-morphism border-0 h-100">
          <div className="card-body text-center">
            <div className="stat-icon">
              <i className="fas fa-heart"></i>
            </div>
            <h3 className="stat-value">{stats.wishlist}</h3>
            <p className="stat-label">Wishlist Items</p>
            <small className="text-muted d-block">Saved for later</small>
          </div>
        </div>
      </div>
      <div className="col-md-3">
        <div className="card glass-morphism border-0 h-100">
          <div className="card-body text-center">
            <div className="stat-icon">
              <i className="fas fa-star"></i>
            </div>
            <h3 className="stat-value">{stats.reviews}</h3>
            <p className="stat-label">Reviews Given</p>
            <small className="text-muted d-block">Help others shop</small>
          </div>
        </div>
      </div>
      <div className="col-md-3">
        <div className="card glass-morphism border-0 h-100">
          <div className="card-body text-center">
            <div className="stat-icon">
              <i className="fas fa-gem"></i>
            </div>
            <h3 className="stat-value">{stats.points.toLocaleString()}</h3>
            <p className="stat-label">Reward Points</p>
            <small className="text-muted d-block">Available for rewards</small>
          </div>
        </div>
      </div>
    </div>
  );

  return (
    <>
      <Helmet>
        <title>Profile - ShopEasy</title>
        <link 
          rel="stylesheet" 
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" 
          integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" 
          crossOrigin="anonymous" 
          referrerPolicy="no-referrer" 
        />
        <style>
          {`
            body {
              background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
              color: #fff;
            }
            .navbar {
              background: rgba(255, 255, 255, 0.05) !important;
              backdrop-filter: blur(10px);
              border-bottom: 1px solid rgba(255, 255, 255, 0.1);
            }
            .navbar-brand {
              color: #fff !important;
              font-weight: 600;
            }
            .nav-link {
              color: rgba(255, 255, 255, 0.7) !important;
              transition: color 0.3s ease;
            }
            .nav-link:hover {
              color: #fff !important;
            }
            .navbar-toggler {
              border-color: rgba(255, 255, 255, 0.1);
            }
            .navbar-toggler-icon {
              background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%28255, 255, 255, 0.7%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
            }
            .navbar-toggler:focus {
              box-shadow: 0 0 0 0.25rem rgba(255, 255, 255, 0.1);
            }
            .text-dark {
              color: rgba(255, 255, 255, 0.7) !important;
            }
            .text-dark:hover {
              color: #fff !important;
            }
            .container {
              background: transparent;
            }
            .glass-morphism {
              background: rgba(255, 255, 255, 0.05);
              backdrop-filter: blur(10px);
              border: 1px solid rgba(255, 255, 255, 0.1);
              position: relative;
              overflow: hidden;
            }
            .glass-morphism::before {
              content: '';
              position: absolute;
              top: 0;
              left: 0;
              right: 0;
              bottom: 0;
              background: linear-gradient(
                45deg,
                rgba(255, 255, 255, 0.1) 0%,
                rgba(255, 255, 255, 0.05) 100%
              );
              z-index: 1;
            }
            .glass-morphism .card-body {
              position: relative;
              z-index: 2;
            }
            .stat-icon {
              width: 60px;
              height: 60px;
              border-radius: 50%;
              background: rgba(255, 255, 255, 0.1);
              display: flex;
              align-items: center;
              justify-content: center;
              margin: 0 auto 1rem;
              transition: all 0.3s ease;
            }
            .stat-icon i {
              font-size: 1.5rem;
              color: #fff;
              transition: all 0.3s ease;
            }
            .card.glass-morphism:hover .stat-icon {
              transform: scale(1.1);
              background: rgba(255, 255, 255, 0.15);
            }
            .card.glass-morphism:hover .stat-icon i {
              transform: scale(1.1);
            }
            .stat-value {
              font-size: 2rem;
              font-weight: 600;
              color: #fff;
              margin-bottom: 0.5rem;
            }
            .stat-label {
              color: rgba(255, 255, 255, 0.7);
              font-size: 0.9rem;
              text-transform: uppercase;
              letter-spacing: 1px;
            }
            .card.glass-morphism {
              transition: all 0.3s ease;
            }
            .card.glass-morphism:hover {
              transform: translateY(-5px);
              box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
            }
            .card.glass-morphism::after {
              content: '';
              position: absolute;
              top: 0;
              left: 0;
              right: 0;
              bottom: 0;
              background: linear-gradient(
                45deg,
                transparent 0%,
                rgba(255, 255, 255, 0.05) 100%
              );
              opacity: 0;
              transition: opacity 0.3s ease;
            }
            .card.glass-morphism:hover::after {
              opacity: 1;
            }
            .text-danger {
              color: #dc3545 !important;
            }
            .text-muted {
              color: rgba(255, 255, 255, 0.5) !important;
            }
            .fw-bold {
              font-weight: 600 !important;
            }
            .small {
              font-size: 0.875rem;
            }
            .d-block {
              display: block !important;
            }
            .mb-1 {
              margin-bottom: 0.25rem !important;
            }
            .mb-0 {
              margin-bottom: 0 !important;
            }
          `}
        </style>
      </Helmet>

      {/* Navbar */}
      <header className="sticky-top">
        <nav className="navbar navbar-expand-lg">
          <div className="container">
            <Link className="navbar-brand" to="/">ShopEasy</Link>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
              <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
              <ul className="navbar-nav me-auto">
                <li className="nav-item">
                  <Link className="nav-link" to="/">Home</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/products">Products</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/about">About</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/contact">Contact</Link>
                </li>
              </ul>
              <div className="d-flex align-items-center gap-3">
                <Link to="/profile" className="text-dark">
                  <i className="fa-solid fa-user fs-5"></i>
                </Link>
                <Link to="/cart" className="text-dark position-relative">
                  <i className="fa-solid fa-cart-shopping fs-5"></i>
                  <CartCount />
                </Link>
              </div>
            </div>
          </div>
        </nav>
      </header>

      <div className="container py-5" style={{ minHeight: '100vh' }}>
        <div className="row">
          {/* Sidebar */}
          <div className="col-md-3 mb-4">
            <div className="card glass-morphism border-0">
              <div className="card-body text-center">
                <img
                  src={user.avatar}
                  alt="Profile"
                  className="rounded-circle mb-3"
                  style={{ 
                    width: '120px', 
                    height: '120px', 
                    objectFit: 'cover',
                    border: '3px solid rgba(255,255,255,0.2)'
                  }}
                />
                <h4 className="text-white mb-1">{user.name}</h4>
                <p className="text-white-50 mb-3">Member since {user.joinDate}</p>
                <div className="d-grid gap-2">
                  <button
                    className={`btn ${activeTab === 'profile' ? 'btn-primary' : 'btn-outline-primary'}`}
                    onClick={() => setActiveTab('profile')}
                  >
                    <i className="fas fa-user me-2"></i>Profile
                  </button>
                  <button
                    className={`btn ${activeTab === 'orders' ? 'btn-primary' : 'btn-outline-primary'}`}
                    onClick={() => setActiveTab('orders')}
                  >
                    <i className="fas fa-shopping-bag me-2"></i>Orders
                  </button>
                  <button
                    className={`btn ${activeTab === 'settings' ? 'btn-primary' : 'btn-outline-primary'}`}
                    onClick={() => {
                      setSelectedOrder(null);
                      setActiveTab('settings');
                    }}
                  >
                    <i className="fas fa-cog me-2"></i>Settings
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* Main Content */}
          <div className="col-md-9">
            {/* Stats Cards */}
            {renderStatsCards()}

            {/* Tab Content */}
            {activeTab === 'profile' && renderProfileContent()}
            {activeTab === 'orders' && renderOrdersContent()}
            {activeTab === 'settings' && renderSettingsContent()}
          </div>
        </div>
      </div>
    </>
  );
}

export default Profile;
