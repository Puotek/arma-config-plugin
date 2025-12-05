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
};
class NormalClass {



    displayName = Q(AN/PRC-163 1);
    x = Q((safeZoneW * 0.5) + safeZoneX - (26 * GRID_W));
    condition = Q(count TAG(targets) > 0);
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
    thingy = 0;
    delete DeleteIsAKeywordTooClass;
    PREPROCESSOR_USED_FOR_PARAMS(something);
    JUST_A_PREPROCESSOR_THING;
    class NoBodyClass;
};
