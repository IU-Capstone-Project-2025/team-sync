import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import HomeHeader from "../components/homeHeader";
import Footer from "../components/footer";
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import { getPersonById, getSkills, getRoles } from "../utils/backendFetching";

interface Person {
  type: "STUDENT" | "TEACHER";
  person: {
    id: number;
    name: string;
    surname: string;
    email: string;
  }
  student_profile: {
    study_group: {
      id: number;
      name: string;
    }
    description: string;
    github_alias: string;
    tg_alias: string;
    skills: number[];
    roles: number[];
  }
  professorProfile: {
    tgAlias: string;
  }
}

export default function UserProfileScreen() {
  const { userID } = useParams();
  const navigate = useNavigate();
  const [user, setUser] = useState<Person | null>(null);
  const [skills, setSkills] = useState<{id: number, name: string}[]>([]);
  const [roles, setRoles] = useState<{id: number, name: string}[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUserData = async () => {
      const token = localStorage.getItem("backendToken");
      if (token && userID) {
        try {
          const userData = await getPersonById(token, parseInt(userID));
          setUser(userData);
        } catch (error) {
          console.error("Failed to fetch user:", error);
        }
      }
      setLoading(false);
    };

    const fetchSkillsAndRoles = async () => {
      try {
        const [skillsData, rolesData] = await Promise.all([
          getSkills(),
          getRoles()
        ]);
        setSkills(skillsData);
        setRoles(rolesData);
      } catch (error) {
        console.error("Failed to fetch skills/roles:", error);
      }
    };

    fetchUserData();
    fetchSkillsAndRoles();
  }, [userID]);

  const getSkillNames = (skillIds: number[]) => {
    return skillIds.map(id => skills.find(skill => skill.id === id)?.name || "Unknown").join(", ");
  };

  const getRoleNames = (roleIds: number[]) => {
    return roleIds.map(id => roles.find(role => role.id === id)?.name || "Unknown").join(", ");
  };

  if (loading) {
    return (
      <div className="flex flex-col min-h-screen">
        <HomeHeader />
        <div className="flex-1 flex items-center justify-center">
          <p className="text-(--secondary-color) text-xl">Loading...</p>
        </div>
        <Footer />
      </div>
    );
  }

  if (!user) {
    return (
      <div className="flex flex-col min-h-screen">
        <HomeHeader />
        <div className="flex-1 flex items-center justify-center">
          <p className="text-(--secondary-color) text-xl">User not found</p>
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="flex flex-col min-h-screen">
      <HomeHeader />
      <div className="flex flex-col flex-1 pt-32">
        <div className="flex flex-col px-18">
          <div className="flex flex-row justify-start items-center py-5">
            <ArrowBackIosIcon 
              onClick={() => navigate(-1)} 
              sx={{ fontSize: 24 }} 
              className="cursor-pointer text-[color:var(--secondary-color)]"
            />
            <h2 className="font-[Inter] text-(--primary-color) text-xl ml-2">
              User Profile
            </h2>
          </div>
          
          <div className="bg-(--header-footer-color) rounded-2xl p-8 border-(--secondary-color) border-1">
            <div className="mb-6">
              <h1 className="font-[Manrope] font-extrabold text-4xl text-(--secondary-color) mb-2">
                {user.person.name} {user.person.surname}
              </h1>
              <p className="font-[Inter] text-lg text-(--secondary-color)">
                {user.person.email}
              </p>
              <p className="font-[Inter] text-lg text-(--secondary-color)">
                {user.type.substring(0, 1) + user.type.toLowerCase().substring(1)}
              </p>
            </div>

            {user.type === "STUDENT" && (
              <div className="space-y-6">
                <div>
                  <h3 className="font-[Inter] font-bold text-xl text-(--secondary-color) mb-2">
                    Study Group
                  </h3>
                  <p className="font-[Inter] text-lg text-(--secondary-color)">
                    {user.student_profile.study_group.name}
                  </p>
                </div>

                <div>
                  <h3 className="font-[Inter] font-bold text-xl text-(--secondary-color) mb-2">
                    Description
                  </h3>
                  <p className="font-[Inter] text-lg text-(--secondary-color)">
                    {user.student_profile.description || "No description provided"}
                  </p>
                </div>

                <div>
                  <h3 className="font-[Inter] font-bold text-xl text-(--secondary-color) mb-2">
                    Contact Information
                  </h3>
                  <div className="space-y-1">
                    <p className="font-[Inter] text-lg text-(--secondary-color)">
                      GitHub: {user.student_profile.github_alias || "Not provided"}
                    </p>
                    <p className="font-[Inter] text-lg text-(--secondary-color)">
                      Telegram: {user.student_profile.tg_alias || "Not provided"}
                    </p>
                  </div>
                </div>

                <div>
                  <h3 className="font-[Inter] font-bold text-xl text-(--secondary-color) mb-2">
                    Skills
                  </h3>
                  <div className="flex flex-wrap gap-2 mt-3">
                    {user.student_profile.skills.length > 0 ? (
                      user.student_profile.skills.map(skillId => {
                        const skill = skills.find(s => s.id === skillId);
                        return skill ? (
                          <span key={skill.id} className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg px-3 py-1 w-fit">
                            {skill.name}
                          </span>
                        ) : null;
                      })
                    ) : (
                      <span className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg px-3 py-1 w-fit">
                        No skills listed
                      </span>
                    )}
                  </div>
                </div>

                <div>
                  <h3 className="font-[Inter] font-bold text-xl text-(--secondary-color) mb-2">
                    Roles
                  </h3>
                  <div className="flex flex-wrap gap-2 mt-3">
                    {user.student_profile.roles.length > 0 ? (
                      user.student_profile.roles.map(roleId => {
                        const role = roles.find(r => r.id === roleId);
                        return role ? (
                          <span key={role.id} className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg px-3 py-1 w-fit">
                            {role.name}
                          </span>
                        ) : null;
                      })
                    ) : (
                      <span className="font-[Inter] text-(--secondary-color) border-(--accent-color-2) border-3 rounded-lg px-3 py-1 w-fit">
                        No roles listed
                      </span>
                    )}
                  </div>
                </div>
              </div>
            )}

            {user.type === "TEACHER" && user.professorProfile && (
              <div>
                <h3 className="font-[Inter] font-bold text-xl text-(--secondary-color) mb-2">
                  Contact Information
                </h3>
                <p className="font-[Inter] text-lg text-(--secondary-color)">
                  Telegram: {user.professorProfile.tgAlias || "Not provided"}
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
} 