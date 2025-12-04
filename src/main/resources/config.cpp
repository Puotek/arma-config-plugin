#include "test.hpp"

// Multiline define with backslashes
#define MYTEST EDERERE \
    EDERERE \
    AETNO

// Single-line define
#define MY_SINGLE TEST_SINGLE

class CfgPatches {
    class ADDON {
        name = "QPRETTY";
        author = "Puotek";
        requiredAddons[] = {};
        requiredVersion = 2.20;
        units[] = {};
        weapons[] = {};
    };
};
// line comment

/*
big comment
    class MyCarBase {
        displayName = "Base Car";
        maxSpeed = 100;
    };
*/
class NoBodyClass;
class CfgPatches {
    class myMod {
        units = MyCar;
        // array parameter
        requiredAddon[] = {"cba_main", "ace_main"};
        version = 1;
        displayName = "My Mod";
    };
};

// top-level inheritance
class CfgVehicles : BaseCfg {
    // simple inheritance
    class MyCarBase : TestBase {
        displayName = "Base Car";
        maxSpeed = 100;
    };

    class MyCar : MyCarBase {
        displayName = "My Car";
        maxSpeed = 120;

        class Turrets {
            class MainTurret {
                weapons = HMG_127;
                magazines[] = {Mag_127, Mag_127_Tracer};
            };

            class CommanderTurret : MainTurret {
                weapons = GMG_40mm;
                magazines[] = {Mag_40mm_HE, Mag_40mm_SMK};
            };
        };

        class EventHandlers {
            // string with spaces and quotes
            init = "hint 'MyCar initialized';";
        };
    };

    class myTruck {
        displayName = "My Truck";
        maxSpeed = 80;
    };
};

class cfgWeapons {
    class MyRifle : Rifle_Base {
        displayName = "My Rifle";
        magazine = Mag_65mm;
        magazines[] = {Mag_65mm, Mag_65mm_Tracer};
    };

    class MyPistol : Pistol_Base {
        displayName = "My Pistol";
        magazine = Mag_9mm;
        magazines[] = {Mag_9mm, Mag_9mm_Subsonic};
    };
};


#include "define.hpp"

class CfgPatches {
    class ADDON {
        name = QPRETTY;
        author = "Puotek";
        skipWhenMissingDependencies = 1;
        requiredAddons[] = {"cba_main", "ace_common", "RS_Objects_Training"};
        requiredVersion = 2.20;
        units[] = {};
        weapons[] = {};
    };
};

class Extended_PostInit_EventHandlers { XEH(postInit); };
class Extended_PreInit_EventHandlers { XEH(preInit); };

class CfgEditorSubcategories {
	class targets_tacdevsqdn4 {
		displayName = "Targets (TACDEVSQDN4)";
	};
};


class CfgVehicles {
x = Q((safeZoneW * 0.5) + safeZoneX - (26 * GRID_W));
allowedHTMLLoadURIs[] += {
"https://discord.gg/*"
};
displayName = Q(AN/PRC-163 1);
MAWLCLS(IR_AIM) = 1;
animPeriod=9.9999997e-006;

    class Man;
    class CAManBase: Man {
        class ACE_SelfActions {
            class TAG(actions) {
                displayName = "Target Manager";
                condition = Q(count TAG(targets) > 0);
                statement = Q([] call FNC(clean););
                icon = QPATH(ui\target_tool.paa);
                class clean {
                    condition = "true";
                    displayName = "Clean all Targets";
                    statement = Q([] call FNC(clean););
                };
                class randomize {
                    condition = "true";
                    displayName = "Randomize Targets";
                    statement = Q([] call FNC(randomize););
                };
                class remove {
                    condition = "true";
                    displayName = "Clear Target List";
                    statement = "";
                    class remove_confirm {
                        condition = "true";
                        displayName = "Confirm?";
                        statement = Q([] call FNC(removeAll););
                        canCollapse = 0;
                    };
                };
            };
        };
    };
    class Helper_Base_F;
    class RS_Training_Cone: Helper_Base_F {
        editorCategory = "EdCat_Things";
        editorSubcategory = "targets_tacdevsqdn4";
        displayName = "Training Cone";
    };
    class RS_Training_Cone_Blue: RS_Training_Cone {
        displayName = "Training Cone (Blue)";
    };
    class Static;
    class RS_Training_IPSC: Static {
        displayName = "IPSC Cardboard Target Stand";
        editorCategory = "EdCat_Things";
        editorSubcategory = "targets_tacdevsqdn4";
        class ACE_Actions {
            class ACE_MainActions {
                distance = 4;
                condition = "true";
                selection = "selectionpos";
                class CleanTarget {
                    displayName = "Clean Target";
                    distance = 4;
                    condition = "true";
                    statement = "_target setDamage 0;";
                    icon = "";
                };
                delete RS_TargetSystem;
                class TAG(add) {
                    displayName = "Add Target";
                    distance = 4;
                    condition = Q(!(_target in TAG(targets)));
                    statement = Q([_target] call FNC(add));
                    icon = "";
                };
                class TAG(remove) {
                    displayName = "Remove Target";
                    distance = 4;
                    condition = Q(_target in TAG(targets));
                    statement = Q([_target] call FNC(remove));
                    icon = "";
                };
                class RS_TargetType {
                    class ShootVertical {
                        displayName = "Shoot (Vertical)";
                        distance = 4;
                        condition = "true";
                        statement = Q(_target setObjectTextureGlobal [0, 'PATH(textures\ipsc_vertical.paa)'];);
                        icon = "";
                    };
                };
            };
        };
    };
};
