import React from 'react';
import Hero from "../Components/Hero";
import FeaturedProducts from "../Components/FeaturedProducts";
import Header from "../Components/Header";
import Footer from "../Components/Footer";
// ...import useEffect, useState as needed...

export default function Home() {
  // Move your JS logic here (e.g., fetch featured products)
  // Use useEffect for side effects
  return (
    <>
      <Header />
      <Hero />
      <FeaturedProducts />
      <Footer />
    </>
  );
}
