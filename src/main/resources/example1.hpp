#include "file.hpp"
#include "includes\file.hpp"

#define SINGLE_LINE_DEFINE SINGLE_LINE_DEFINE
#define MULTI_LINE_DEFINE MULTI_LINE_DEFINE\
MULTI_LINE_DEFINE

//Single line comment
/* Block comment one line */
/*
Block
comment
multiline
*/

//todo works?
//fixme works as well?



class 30Rnd_556x45_Stanag;
class NoBodyClass;
class ErrorsShouldBeShowingHere {
    TEST(var,var);
    arrayWrong = {};
    arrayWrond2[] = test;
}
class TestClass {
    condition = Q(count TAG(targets) > 0);
    condition = Q(!(_target in TAG(targets)));

	indentifier = anotherIdentifer;
	array[] = {}; //inline comment
	arrayMultiline[] = {
	    "test",
        "test"
        ,"test"
	}; //inline comment
	string = "hallo from the other siiide";
    TEST(var,var);
	math = 1 + 1 + (42 - 2);
	float = 2.20;
	number = 12;
    preprocessorThing = Q(path\to\file.paa);
    complexPreprocessor = Q(_target setObjectTextureGlobal [0, 'PATH(textures\ipsc_vertical.paa)'];);
    class NestedClass : extendClass {
		/*Another block comment*/
		array[] = {var, 1, 1, "", 42, 43};
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
