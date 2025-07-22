export default function ContactSection() {
  return (
    <div className="relative text-(--secondary-color) py-32 min-h-[1100px] w-full">
      {/* Background SVG */}
      <div className="absolute inset-0 w-full h-full overflow-hidden">
        <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-[65vw] h-[65vw]">
          <img 
            src="./ellipse.svg" 
            alt="" 
            className="w-full h-full object-contain"
          />
        </div>
      </div>
      
      {/* Content */}
      <div className="relative z-10 w-full max-w-7xl mx-auto px-8 h-full flex flex-col justify-between min-h-[600px]">
        {/* Top text - positioned right */}
        <div className="flex justify-end pt-16">
          <div className="text-right max-w-2xl mr-[150px]">
            <h1 className="font-[Manrope] font-extrabold text-6xl leading-tight">
              Any questions left?<br />
              Feel free to ask!
            </h1>
          </div>
        </div>
        
        {/* Bottom text - positioned left */}
        <div className="flex justify-start pb-16">
          <div className="text-center max-w-4xl ml-[150px]">
            <h2 className="font-[Manrope] font-extrabold text-6xl leading-tight">
              Contact us on Telegram<br />
              @TeamSync
            </h2>
          </div>
        </div>
      </div>
    </div>
  );
}
