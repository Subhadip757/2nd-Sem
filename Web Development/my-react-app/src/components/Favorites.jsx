import React from "react";

function Favorites({ favorites, onPlayStation, onClearFavorites }) {
  return (
    <section className="card shadow-sm mb-4">
      <div className="card-body p-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="h4 mb-0">
            <i className="fas fa-star text-warning me-2"></i>
            Your Favorites
          </h2>
          <button 
            className="btn btn-outline-danger btn-sm"
            onClick={onClearFavorites}
          >
            Clear All
          </button>
        </div>
        
        <div className="favorites-list" style={{ maxHeight: '300px', overflowY: 'auto' }}>
          {favorites.map(station => (
            <div 
              key={station.id} 
              className="card mb-2 favorite-item"
              onClick={() => onPlayStation(station)}
            >
              <div className="card-body">
                <h5 className="card-title mb-1">{station.name}</h5>
                <p className="card-text text-muted small">
                  {station.country} - {station.tags}
                </p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

export default Favorites