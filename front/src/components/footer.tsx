import React from 'react';

export default function Footer(){
  return(
    <div className="bottom-0 left-0 right-0 w-full flex flex-row justify-between bg-(--header-footer-color) p-1 pr-3 pl-3 lg:pr-15 lg:pl-17 mt-5">
      <p className="font-[Manrope] text-(--secondary-color)">
        © 2025 Innopolis University
      </p>
      {/* <p className="font-[Manrope] text-(--secondary-color)">
        Help
      </p> */}
    </div>
  );
}