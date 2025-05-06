import React from "react";

function StationSearch({
    stationSearch,
    setStationSearch,
    onSearchStations,
    countryList,
    countryFilter,
    setCountryFilter,
    genreFilter,
    setGenreFilter,
    stations,
    onPlayStation,
    loadingStations,
    stationsError
  }) {
    return (
      <section className="card shadow-sm">
        <div className="card-body p-4">
          <h2 className="h4 mb-4">
            <i className="fas fa-search me-2 text-primary"></i>
            Find Stations
          </h2>
          
          <div className="input-group mb-4">
            <input
              type="text"
              className="form-control"
              placeholder="Search stations..."
              value={stationSearch}
              onChange={e => setStationSearch(e.target.value)}
            />
            <button 
              className="btn btn-primary" 
              onClick={onSearchStations}
            >
              <i className="fas fa-search"></i>
            </button>
          </div>

          <div className="row g-3 mb-4">
            <div className="col-md-6">
              <label className="form-label">Filter by country:</label>
              <select
                className="form-select"
                value={countryFilter}
                onChange={e => setCountryFilter(e.target.value)}
              >
                <option value="all">All Countries</option>
                {countryList.map(country => (
                  <option key={country} value={country}>{country}</option>
                ))}
              </select>
            </div>
            <div className="col-md-6">
              <label className="form-label">Filter by genre:</label>
              <select
                className="form-select"
                value={genreFilter}
                onChange={e => setGenreFilter(e.target.value)}
              >
                <option value="all">All Genres</option>
                <option value="pop">Pop</option>
                <option value="rock">Rock</option>
                <option value="jazz">Jazz</option>
                <option value="classical">Classical</option>
                <option value="electronic">Electronic</option>
                <option value="news">News</option>
                <option value="sports">Sports</option>
                <option value="talk">Talk</option>
              </select>
            </div>
          </div>

          <div className="stations-list" style={{ maxHeight: '400px', overflowY: 'auto' }}>
            {loadingStations && (
              <div className="text-center py-4">
                <div className="spinner-border text-primary" role="status">
                  <span className="visually-hidden">Loading...</span>
                </div>
              </div>
            )}
            
            {stationsError && (
              <div className="alert alert-danger" role="alert">
                <i className="fas fa-exclamation-circle me-2"></i>
                {stationsError}
              </div>
            )}

            {!loadingStations && !stationsError && stations.map(station => (
              <div 
                key={station.id} 
                className="card mb-2 station-item"
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

  export default StationSearch