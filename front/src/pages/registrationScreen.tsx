import { useState, useEffect } from 'react';
import { getRoles, getSkills } from "../utils/backendFetching";
import CustomizedHook from "../components/autocompleteInput";
import { useRef } from 'react';
import { useNavigate } from 'react-router-dom';

const backendHost = import.meta.env.VITE_BACKEND_HOST;

interface StepArcProps {
  currentStep: number;
  labels: string[];
  diameter?: number;
  color?: string;
}
const StepArc = ({ currentStep, labels, color = '#ffc100', onLabelClick }: StepArcProps & { onLabelClick?: (idx: number) => void }) => {
  const stepArcRef = useRef<HTMLDivElement>(null);
  const [stepArcRadiusPx, setStepArcRadiusPx] = useState(0);
  useEffect(() => {
    function updateRadius() {
      if (stepArcRef.current) {
        setStepArcRadiusPx(stepArcRef.current.offsetWidth / 2);
      }
    }
    updateRadius();
    window.addEventListener('resize', updateRadius);
    return () => window.removeEventListener('resize', updateRadius);
  }, []);

  const arcStart = 162.5;
  const arcEnd = 197.5;
  const getLabelAngle = (idx: number) => {
    const activeIdx = currentStep - 1;
    const baseAngle = arcStart + idx * ((arcEnd - arcStart) / (labels.length - 1));
    const rotate = -(arcStart + activeIdx * ((arcEnd - arcStart) / (labels.length - 1)) - 180);
    return baseAngle + rotate;
  };
  const opacity = currentStep === 1 ? 0.4 : currentStep === 2 ? 0.7 : 1.0;
  return (
    <div
      ref={stepArcRef}
      style={{
        width: '80vw',
        height: '80vw',
        minWidth: 1200,
        minHeight: 1200,
        position: 'fixed',
        right: '-30vw',
        top: '50%',
        transform: 'translateY(-50%)',
        zIndex: 1,
        pointerEvents: 'none',
      }}
    >
      <div
        style={{
          width: '100%',
          height: '100%',
          borderRadius: '50%',
          background: color,
          position: 'absolute',
          left: 0,
          top: 0,
          opacity,
          transition: 'opacity 0.5s',
        }}
      />
      {stepArcRadiusPx > 0 && labels.map((label, idx) => {
        const angle = getLabelAngle(idx);
        const rad = (angle * Math.PI) / 180;
        const labelRadius = stepArcRadiusPx * 0.8;
        const tx = Math.cos(rad) * labelRadius;
        const ty = Math.sin(rad) * labelRadius;
        const isActive = currentStep === idx + 1;
        const rotate = angle + 180;
        return (
          <div
            key={label}
            onClick={onLabelClick ? () => onLabelClick(idx) : undefined}
            style={{
              position: 'absolute',
              left: '50%',
              top: '50%',
              width: 140,
              textAlign: 'center',
              fontWeight: isActive ? 700 : 500,
              fontSize: isActive ? '4rem' : '2.5rem',
              color: isActive ? '#000000' : '#484848',
              background: 'none',
              pointerEvents: 'auto',
              zIndex: isActive ? 2 : 1,
              transition: 'font-size 0.2s, font-weight 0.2s, color 0.2s, transform 0.6s cubic-bezier(0.77,0,0.175,1)',
              willChange: 'transform',
              transform: `translate(-50%, -50%) translate(${tx}px, ${ty}px) rotate(${rotate}deg)`,
              transformOrigin: '120px 50%',
              cursor: 'pointer',
              userSelect: 'none',
            }}
            onMouseOver={e => {
              if (!isActive) {
              (e.currentTarget as HTMLDivElement).style.color = '#484848';
              }
            }}
            onMouseOut={e => {
              (e.currentTarget as HTMLDivElement).style.color = isActive ? '#000000' : '#7A7979';
            }}
          >
            {label}
          </div>
        );
      })}
    </div>
  );
};

export default function RegistrationScreen() {
  const [step, setStep] = useState(1);
  const labels = ["Skills", "Project Position", "Finalization"];
  const maxStep = labels.length;
  const navigate = useNavigate();
  const [roles, setRoles] = useState<{id: number, name: string, description: string}[]>([]);
  const [skills, setSkills] = useState<{id: number, name: string, description: string}[]>([]);
  const [selectedRoles, setSelectedRoles] = useState<number[]>([]);
  const [selectedSkills, setSelectedSkills] = useState<number[]>([]);
  const [formData, setFormData] = useState({
    skills: [],
    roles: [],
    tg_alias: '',
    github_alias: '',
    study_group: '',
    description: ''
  });

  useEffect(() => {
    getSkills().then(setSkills);
    getRoles().then(setRoles);
  }, []);

  useEffect(() => {
    setFormData(prev => ({
      ...prev,
      skills: selectedSkills,
    }));
  }, [selectedSkills]);

  useEffect(() => {
    setFormData(prev => ({
      ...prev,
      roles: selectedRoles,
    }));
  }, [selectedRoles]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const accessToken = localStorage.getItem("entraToken");
    try {
      const regRes = await fetch(`${backendHost}/auth/api/v1/entra/registration/student`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
      });
      const regData = await regRes.json();
      if (regData.success && regData.data?.access_token) {
        localStorage.setItem("backendToken", regData.data.access_token);
        navigate('/home');
      } else {
        console.error("Registration failed", regData.error || regData);
      }
    } catch (error) {
      console.error("Registration error:", error);
    }
  };

  const nextStep = (e: React.MouseEvent) => setStep(s => Math.min(maxStep, s + 1));
  const prevStep = (e: React.MouseEvent) => setStep(s => Math.max(1, s - 1));

  const handleChange = (field: keyof typeof formData, value: any) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const renderStep = () => {
    switch(step) {
      case 1:
        return (
          <div>
            <h2 className="text-(--secondary-color) font-[Manrope] font-extrabold text-5xl mb-5">What skills do you have?</h2>
            <h3 className="w-[60%] text-(--secondary-color) font-[Inter] font-light text-xl mb-3">This helps us recommend the right projects and teammates. You can skip this for now.</h3>
            {(skills.length > 0) ? 
              <CustomizedHook
              arr={skills}
              value={skills.filter(skill => selectedSkills.includes(skill.id))}
              onChange={items => setSelectedSkills(items.map(item => item.id))}
              /> : <div></div>
            }
          </div>
        );
      case 2:
        return (
          <div>
            <h2 className="text-(--secondary-color) font-[Manrope] font-extrabold text-5xl mb-5">What role would you like to take in a project?</h2>
            <h3 className="w-[70%] text-(--secondary-color) font-[Inter] font-light text-xl mb-3">Tell us how you'd like to contribute — this helps others find you more easily. You can skip this for now.</h3>
            <CustomizedHook
              arr={roles}
              value={roles.filter(role => selectedRoles.includes(role.id))}
              onChange={items => setSelectedRoles(items.map(item => item.id))}
            />
          </div>
        );
      case 3:
        return (
          <div>
            <h2 className="text-(--secondary-color) font-[Manrope] font-extrabold text-5xl mb-5">Let's connect your profile</h2>
            <h3 className="w-[70%] text-(--secondary-color) font-[Inter] font-light text-xl">These details help teammates contact you faster.</h3>
            <div className="py-5 flex flex-col gap-2">
              <label htmlFor="tg_alias">Telegram alias</label>
              <input
                className="focus:border-(--accent-color-2) focus:outline-none border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md w-[20%]"
                type="text"
                id="tg_alias"
                value={formData.tg_alias}
                onChange={(e) => handleChange('tg_alias', e.target.value)}
                required
              />
              <label className = "font-[Inter] text-md" htmlFor="github_alias">GitHub link</label>
              <input
                className="focus:border-(--accent-color-2) focus:outline-none border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md w-[35%]"
                type="link"
                id="github_alias"
                value={formData.github_alias}
                onChange={(e) => handleChange('github_alias', e.target.value)}
                required
              />
              <label htmlFor="study_group">Study group</label>
              <input
                className="focus:border-(--accent-color-2) focus:outline-none border-(--secondary-color) border-2 rounded-2xl min-h-10 min-w-50 p-1 text-(--secondary-color) font-[Inter] text-md w-[20%]"
                type="text"
                id="study_group"
                value={formData.study_group}
                onChange={(e) => handleChange('study_group', e.target.value)}
                required
              />
              <label htmlFor="description">About me</label>
              <textarea
                className="focus:border-(--accent-color-2) focus:outline-none border-(--secondary-color) border-2 rounded-2xl p-1 text-(--secondary-color) font-[Inter] text-md w-[60%] max-h-40 min-h-10"
                id="description"
                value={formData.description}
                onChange={(e) => handleChange('description', e.target.value)}
              />
            </div>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="text-black flex flex-col h-screen">
      <svg className='lg:w-42 lg:h-18 w-50 h-20 ml-15 mt-15' xmlns="http://www.w3.org/2000/svg" fill="none">
        <path d="M89.8662 4.88852V9.53262H82.3228V27.9344H76.288V9.53262H68.7095V4.88852H89.8662Z" fill="#FFC100"/>
        <path d="M93.3158 27.9344V4.88852H111.35V9.53262H99.3506V14.2116H108.894V18.5764H99.3506V23.2903H111.806V27.9344H93.3158Z" fill="#FFC100"/>
        <path d="M136.501 27.9344H130.291L128.677 22.7316H120.748L119.099 27.9344H113.029L121.169 4.88852H128.397L136.501 27.9344ZM121.871 18.6462H127.555L124.713 9.32311L121.871 18.6462Z" fill="#FFC100"/>
        <path d="M166 4.88852V27.9344H160.667V17.3543L160.842 10.2659H160.772L155.053 27.9344H150.176L144.457 10.2659H144.387L144.563 17.3543V27.9344H139.195V4.88852H147.791L151.439 16.621L152.738 21.5095H152.808L154.141 16.6559L157.755 4.88852H166Z" fill="#FFC100"/>
        <path d="M79.4457 45.2771C81.5977 45.2771 83.4923 45.6146 85.1296 46.2897C86.7904 46.9415 88.2289 47.9075 89.4452 49.1879L86.5331 53.0638C85.5273 52.0162 84.4396 51.2364 83.2701 50.7243C82.124 50.1889 80.8141 49.9212 79.3405 49.9212C78.475 49.9212 77.7733 50.0143 77.2353 50.2005C76.6974 50.3867 76.3114 50.6428 76.0775 50.9687C75.8436 51.2946 75.7267 51.667 75.7267 52.0861C75.7267 52.5749 75.9489 52.9939 76.3933 53.3431C76.8611 53.669 77.6096 53.9484 78.6388 54.1811L82.5333 55.0541C84.8489 55.5895 86.5448 56.3693 87.6207 57.3936C88.6967 58.4179 89.2347 59.7797 89.2347 61.479C89.2347 63.1085 88.8136 64.4936 87.9716 65.6343C87.1295 66.7516 85.9717 67.6013 84.4981 68.1833C83.0245 68.742 81.3404 69.0213 79.4457 69.0213C77.902 69.0213 76.4284 68.8584 75.0249 68.5325C73.6215 68.1833 72.3584 67.6944 71.2357 67.0659C70.1129 66.4374 69.1656 65.7157 68.3937 64.901L71.3059 60.8854C71.8906 61.5605 72.604 62.1657 73.4461 62.7011C74.3115 63.2133 75.2472 63.6207 76.2529 63.9233C77.2821 64.2259 78.323 64.3772 79.3756 64.3772C80.1942 64.3772 80.8726 64.2957 81.4105 64.1328C81.9719 63.9698 82.3813 63.7254 82.6385 63.3995C82.8958 63.0736 83.0245 62.6895 83.0245 62.2472C83.0245 61.7584 82.8491 61.351 82.4982 61.0251C82.1473 60.6759 81.4807 60.3849 80.4983 60.1521L76.2179 59.2093C74.908 58.9067 73.7385 58.511 72.7093 58.0221C71.7035 57.51 70.9082 56.8349 70.3235 55.9969C69.7387 55.1356 69.4463 54.0648 69.4463 52.7844C69.4463 51.3411 69.844 50.0608 70.6392 48.9434C71.4345 47.8028 72.5806 46.9066 74.0776 46.2548C75.5746 45.603 77.364 45.2771 79.4457 45.2771Z" fill="#484848"/>
        <path d="M112.53 45.6262L104.355 60.2569V68.6721H98.3202V60.2569L90.1452 45.6262H96.3905L99.6535 51.9464L101.373 55.7874L103.127 51.9464L106.39 45.6262H112.53Z" fill="#484848"/>
        <path d="M136.052 45.6262V68.6721H129.351L122.158 56.2064L120.404 52.7844H120.369L120.509 57.0444V68.6721H115.176V45.6262H121.877L129.07 58.092L130.824 61.5139H130.859L130.719 57.2539V45.6262H136.052Z" fill="#484848"/>
        <path d="M161.707 62.0726C161.263 63.6789 160.549 65.0057 159.567 66.0533C158.584 67.0775 157.415 67.8341 156.058 68.323C154.701 68.7885 153.205 69.0213 151.567 69.0213C149.228 69.0213 147.205 68.5557 145.497 67.6246C143.79 66.6702 142.48 65.3084 141.568 63.5392C140.656 61.77 140.199 59.64 140.199 57.1492C140.199 54.6584 140.656 52.5284 141.568 50.7592C142.48 48.99 143.79 47.6398 145.497 46.7087C147.205 45.7543 149.216 45.2771 151.532 45.2771C153.146 45.2771 154.631 45.5098 155.988 45.9754C157.345 46.4177 158.491 47.1044 159.426 48.0356C160.362 48.9667 161.029 50.1656 161.426 51.6321L156.128 53.7621C155.801 52.3189 155.263 51.3295 154.514 50.7941C153.789 50.2587 152.865 49.991 151.743 49.991C150.643 49.991 149.684 50.2587 148.866 50.7941C148.07 51.3295 147.45 52.1326 147.006 53.2034C146.585 54.251 146.374 55.5662 146.374 57.1492C146.374 58.7089 146.573 60.0241 146.971 61.0949C147.369 62.1657 147.965 62.9689 148.76 63.5043C149.579 64.0397 150.585 64.3074 151.778 64.3074C152.9 64.3074 153.848 64.0048 154.62 63.3995C155.415 62.771 155.953 61.8166 156.234 60.5362L161.707 62.0726Z" fill="#484848"/>
        <path d="M55.1092 46.0486C55.1092 36.7497 47.6351 29.2114 38.4155 29.2114C29.1959 29.2114 21.7219 36.7497 21.7219 46.0486C21.7219 55.3475 29.1959 62.8857 38.4155 62.8857V71C24.7527 71 13.6767 59.8289 13.6767 46.0486C13.6767 32.2683 24.7527 21.0971 38.4155 21.0971C52.0784 21.0971 63.1543 32.2683 63.1543 46.0486C63.1543 59.8289 52.0784 71 38.4155 71V62.8857C47.6351 62.8857 55.1092 55.3475 55.1092 46.0486Z" fill="#484848"/>
        <path d="M41.4324 24.9514C41.4324 15.6525 33.9584 8.11429 24.7388 8.11429C15.5191 8.11429 8.04513 15.6525 8.04513 24.9514C8.04513 34.2503 15.5191 41.7886 24.7388 41.7886V49.9029C11.0759 49.9029 0 38.7317 0 24.9514C0 11.1711 11.0759 0 24.7388 0C38.4016 0 49.4776 11.1711 49.4776 24.9514C49.4776 38.7317 38.4016 49.9029 24.7388 49.9029V41.7886C33.9584 41.7886 41.4324 34.2503 41.4324 24.9514Z" fill="#FFC100"/>
      </svg>
      <div className="flex flex-1 items-center min-h-0 ml-15">
        <form className="w-[50%]" onSubmit={handleSubmit}>
          {renderStep()}
          <div className="form-navigation mt-8 flex gap-6">
            {step > 1 && (
              <button type="button" onClick={e => prevStep(e)} className="px-6 py-3 rounded-xl bg-(--primary-color) text-(--header-footer-color) text-xl font-bold hover:bg-(--secondary-color) transition-all">Back</button>
            )}
            {step < 3 && (
              <button type="button" onClick={e => nextStep(e)} className="px-6 py-3 rounded-xl bg-(--accent-color-2) text-(--header-footer-color) text-xl font-bold hover:bg-(--accent-color-2)/60 transition-all">Continue</button>
            )}
            {step === 3 && (
              <button type="submit" className="px-6 py-3 rounded-xl bg-(--accent-color-2) text-(--header-footer-color) text-xl font-bold hover:bg-(--accent-color-2)/60 transition-all">Submit</button>
            )}
          </div>
        </form>
        <StepArc currentStep={step} labels={labels} diameter={1200} color="#f9c846" onLabelClick={idx => setStep(idx + 1)} />
      </div>
    </div>
  );
}