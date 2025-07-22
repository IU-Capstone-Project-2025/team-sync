import React from 'react';
import { useState } from 'react'
import { useNavigate } from 'react-router-dom';

export default function NavButton({text, link} : {text : string, link : string}) {
  const navigate = useNavigate();
  
  const handleClick = () => {
    // Если ссылка начинается с #, то это якорная ссылка для прокрутки
    if (link.startsWith('#')) {
      const element = document.getElementById(link.substring(1));
      if (element) {
        element.scrollIntoView({ 
          behavior: 'smooth',
          block: 'start'
        });
      }
    } else {
      // Иначе обычная навигация
      navigate(link);
    }
  };

  return (
    <div>
      <h3 className = "font-[Inter] text-xs lg:text-xl text-(--secondary-color) hover:underline cursor-pointer" 
      onClick={handleClick}>
        {text}
      </h3>
    </div>
  );
}