import React from 'react'

function Hero() {
    return (
        <>
            <div className="bg-danger text-white py-5">
                <div className="container">
                    <div className="row align-items-center">
                        <div className="col-md-6">
                            <h1 className="display-4 fw-bold">Welcome to ShopEasy</h1>
                            <p className="lead">Discover Amazing Deals on Trending Products</p>
                            <a href="#featured" className="btn btn-light btn-lg">View Products</a>
                        </div>
                        <div className="col-md-6">
                            <img src="https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da" className="img-fluid rounded" alt="Shopping" />
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Hero