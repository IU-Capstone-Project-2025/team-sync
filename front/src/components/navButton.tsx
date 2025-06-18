import { useState } from 'react'

export default function NavButton({text, link}) {
  return (
    <div>
      <h3 className = "font-[Inter] text-xs lg:text-xl text-(--secondary-color)">
        {text}
      </h3>
    </div>
  );
}