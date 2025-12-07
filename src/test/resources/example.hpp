#include "strings.hpp"
#include "test\test.hpp"
#include "..\test.hpp"
#include "\mymod\myaddon\strings.hpp"
#include <strings.hpp>
#include <test\test.hpp>
#include <\mymod\myaddon\strings.hpp>

#ifdef
#ifndef
#else
#undef
#if
#endif

#define SINGLE_LINE_DEFINE SINGLE_LINE_DEFINE
    #define MULTI_LINE_DEFINE MULTI_LINE_DEFINE\
MULTI_LINE_DEFINE

class SingleLineClass { MACRO(include); };
class 01ClassStartingWithNumber;
class NoBodyClass;
class InspectionTestingClass {
    TEST(var,var);
    duplicatedParam = 1;
    duplicatedParam = 1;
    trailingComma[] = {"hi",};
};
class NormalClass {

    value = 10 + -10;   // = 0
value = 10 + +5;    // = 15
value = 10 * -2;    // = -20
value = -10 * -10;  // = 100
value = ---10;    // valid → result = -10
value = --5;      // valid → result = +5
value = -----3;   // valid → result = -3
value = 1e5;
value = 3.14e-2;
value = 6.02E23;
value = 10E+6;
value = 0E0;

    x = Q((safeZoneW * 0.5) + safeZoneX - (26 * GRID_W));
    allowedHTMLLoadURIs[] += {
        "https://discord.gg/*"
    };
    MAWLCLS(IR_AIM) = 1;
    displayName = Q(AN/PRC-163 1);
    animPeriod=9.9999997e-006;
    condition = QUOTE((_this select 0) call FUNC(canMovePack) && {backpack (_this select 0) != '' || {(_this select 0) call FUNC(chestpack) != ''}});
    tabs[] = {{},{4}};
    allowedPositions[] = {"driver", {"turret", {1}}};

    class PREFIX##_Vehicles {
        displayName = CSTRING(Category);
    };
    TAG##test = 1;
    samples[]= {
        {"\A3\Sounds_F\arsenal\weapons\LongRangeRifles\DMR_01_Rahim\DMR01_silencerShot_01",1},
        {"\A3\Sounds_F\arsenal\weapons\LongRangeRifles\DMR_01_Rahim\DMR01_silencerShot_02",1},
        {"\A3\Sounds_F\arsenal\weapons\LongRangeRifles\DMR_01_Rahim\DMR01_silencerShot_03",1}
    };
    displayName = Q(AN/PRC-163 1);
    x = Q((safeZoneW * 0.5) + safeZoneX - (26 * GRID_W));
    condition3 = Q(count TAG(targets) > 0);
    condition2 = Q(!(_target in TAG(targets)));
    indentifier = anotherIdentifer;
    array[] = {

    };
    //inline comment
    arrayMultiline[] = {
        "test",
        "test"
        , "test"
    };
    /*
block
comment
*/
    string1 = "Here is a ""string""";
    string2 = 'Here is a "substring"';
    string3 = "Here is a 'string'";
    TEST(var,var);
    math = 1 + 1 + (42 - 2);
    float = 2.20;
    number = 12;
    preprocessorThing = Q(path\to\file.paa);
    complexPreprocessor = Q(_target setObjectTextureGlobal [0, 'PATH(textures\ipsc_vertical.paa)'];);
    class NestedClass : extendClass {
        /*Another block comment*/
        array[] = {var, 1, 1e1, "", 42, 43};
        math = 1 + 69 * 1;
    };
    class 30Rnd_556x45_Stanag;
    class test : 30Rnd_556x45_Stanag {};
    class TAG(preprocessorClass) : TAG(extensionAlso) {};
    delete DeleteIsAKeywordTooClass;
    PREPROCESSOR_USED_FOR_PARAMS(something);
    class NoBodyClass;
};
