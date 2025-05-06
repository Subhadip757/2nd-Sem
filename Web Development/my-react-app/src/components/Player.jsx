import React from "react";

function Player({
    nowPlaying,
    volume,
    onVolumeChange,
    onPlay,
    onStop,
    onFavorite,
    isPlaying,
    isFavorite,
    stationName,
    stationLocation,
    visualizerRef
  }) {
    return (
      <section className="card shadow-sm mb-4">
        <div className="card-body p-4">
          <div className="d-flex justify-content-between align-items-center mb-4">
            <div className="text-truncate" role="status">
              <h5 className="mb-0">{nowPlaying}</h5>
            </div>
            <div className="d-flex align-items-center">
              <i className="fas fa-volume-down text-muted me-2"></i>
              <input
                type="range"
                className="form-range"
                min="0"
                max="1"
                step="0.1"
                value={volume}
                onChange={onVolumeChange}
                style={{ width: '100px' }}
              />
            </div>
          </div>
          
          <div className="d-flex justify-content-center gap-3 mb-4">
            <button 
              className="btn btn-primary btn-lg rounded-pill px-4" 
              onClick={onPlay}
            >
              <i className={`fas ${isPlaying ? 'fa-pause' : 'fa-play'} me-2`}></i>
              {isPlaying ? 'Pause' : 'Play'}
            </button>
            <button 
              className="btn btn-outline-danger btn-lg rounded-pill px-4" 
              onClick={onStop}
            >
              <i className="fas fa-stop me-2"></i> Stop
            </button>
            <button 
              className="btn btn-outline-warning btn-lg rounded-pill px-4" 
              onClick={onFavorite}
            >
              <i className={`fa${isFavorite ? 's' : 'r'} fa-star me-2`}></i>
              {isFavorite ? 'Unfavorite' : 'Favorite'}
            </button>
          </div>

          <div className="text-center">
            <h3 className="mb-2">{stationName}</h3>
            <p className="text-muted mb-3">{stationLocation}</p>
            <div className="d-flex justify-content-center align-items-center">
              <div className="signal-bars me-2">
                <div className="bar"></div>
                <div className="bar"></div>
                <div className="bar"></div>
                <div className="bar"></div>
                <div className="bar"></div>
              </div>
              <span className="text-muted">Connecting...</span>
            </div>
          </div>
          
          <div ref={visualizerRef} className="mt-4" style={{ height: '100px' }}></div>
        </div>
      </section>
    );
  }

  export default Player;