import React from "react";

function RecentlyPlayed({ recentlyPlayed, onPlayStation }) {
  return (
    <section className="card shadow-sm">
      <div className="card-body p-4">
        <h2 className="h4 mb-4">
          <i className="fas fa-history text-primary me-2"></i>
          Recently Played
        </h2>
        
        <div className="recent-list" style={{ maxHeight: '300px', overflowY: 'auto' }}>
          {recentlyPlayed.map(station => (
            <div 
              key={station.id} 
              className="card mb-2 recent-item"
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

export default RecentlyPlayed;