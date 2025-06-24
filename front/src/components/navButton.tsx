import { useState } from 'react'
import { useNavigate } from 'react-router-dom';

export default function NavButton({text, link} : {text : string, link : string}) {
  const navigate = useNavigate();
  return (
    <div>
      <h3 className = "font-[Inter] text-xs lg:text-xl text-(--secondary-color) hover:underline cursor-pointer" 
      onClick={() => navigate(link)}>
        {text}
      </h3>
    </div>
  );
}