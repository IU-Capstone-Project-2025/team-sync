import React from 'react';
import './imageBanner.css';

const duplicates = 8;
const images = Array.from({ length: duplicates });

const SlidingBanner = () => (
  <div className="banner-outer">
    <div className="banner-inner">
      {/* Render multiple sets for seamless looping */}
      {[...images, ...images, ...images].map((_, i) => (
        <img
          key={i}
          src="../innopolis.png"
          className="banner-img"
          alt="Innopolis Logo"
        />
      ))}
    </div>
  </div>
);

export default SlidingBanner;