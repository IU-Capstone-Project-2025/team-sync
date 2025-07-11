import React, { useState, useRef, useEffect } from 'react';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { useNavigate } from "react-router-dom";
import { useMsal } from '@azure/msal-react';

export default function Account({name}){
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  const { instance } = useMsal();

  // Close menu when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setIsMenuOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <div className='relative flex flex-row items-center gap-1' ref={menuRef}>
      <span className="bg-(--secondary-color) rounded-4xl min-w-13 min-h-13 inline-block"></span>
      <h3 className='font-[Inter] text-(--secondary-color) font-bold text-xl pl-3'>
        {name}
      </h3>
      <ExpandMoreIcon 
        sx={{ 
          fontSize: 30,
          transform: isMenuOpen ? 'rotate(180deg)' : 'rotate(0deg)',
          transition: 'transform 0.2s ease'
        }} 
        className="text-[color:var(--secondary-color)] cursor-pointer"
        onClick={toggleMenu}
      />
      
      {isMenuOpen && (
        <div className="absolute top-full right-0 mt-2 w-48 bg-(--header-footer-color) border border-(--primary-color) rounded-lg shadow-lg z-10">
          <div className="py-2">
            <button 
              className="w-full text-left px-4 py-2 text-(--secondary-color) hover:bg-(--accent-color-2)/20 font-[Inter]"
              onClick={() => {
                console.log('Profile clicked');
                setIsMenuOpen(false);
                navigate('/profile');
              }}
            >
              Profile
            </button>
            <hr className="my-2 border-(--primary-color)" />
            <button 
              className="w-full text-left px-4 py-2 text-(--secondary-color) hover:bg-(--accent-color-2)/20 font-[Inter]"
              onClick={() => {
                console.log('Logout clicked');
                setIsMenuOpen(false);
                instance.logoutPopup().then(()=>{
                  localStorage.removeItem("backendToken");
                  console.log('cleared token');
                  navigate('/')
                });
              }}
            >
              Logout
            </button>
          </div>
        </div>
      )}
    </div>
  );
}