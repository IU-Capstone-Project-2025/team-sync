import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

export default function Account({name}){
  return (
    <div className='flex flex-row items-center gap-1'>
      <span className="bg-(--secondary-color) rounded-4xl min-w-13 min-h-13 inline-block"></span>
      <h3 className='font-[Inter] text-(--secondary-color) font-bold text-xl pl-3'>
        {name}
      </h3>
      <ExpandMoreIcon sx={{ fontSize: 30 }} className="text-[color:var(--secondary-color)]"/>
    </div>
  );
}