/*
DoubleQuote string ("") will parse anything inside and if it encounters double DoubleQuote "" it will escape it and it wont close string on first DoubleQuote

SingleQuote is some dark magic:
It will encase itself with "" but keep the original '' (so basiclly adds " at front and at the end)
Any " it encounters will be converted to a "" (doubled)
It will ignore any ' inside, they can have even an odd ammount of them inside
It will still have string parsing inside, so if there is a string inside it will be checked first, since '' is a preprocessor thing, basiclly if there is a valid "" inside anything inside it will be skipped, if there is an invalid string inside, it might cause an unclosed string and cause errors
It will still process any macros properly, before quoting itself

If a '' is used inside another macro, espescially one that quotes, it will be resolved similar to normal macros apparently, so basiclly when macros are resolved and it ends up inside a string, it will do nothing, but if it ends up surrounding stuff as a value of a parameter like var='something'; than it will act same as normal, described above

When it comes to a macro that quotes using #, it basiclly works the same as normal string, however it allows for macro usage and it processes macros inside of it. It basiclly just adds " at the start and end.
*/

//class CfgPatches {...};

#define SQ(var) #var
#define DQ(var) #SQ(var)
#define MACRO(var) var

/*
Testing was done by basiclly having only one parameter uncommented and than binarizing the config.cpp using arma 3 tools and than unbinarizing to see the output
The output is always in the comment on the parameter and if config failed to binarize it will say 'invalid'
*/


class StringTest {

    advExample = SQ((_this select 0) call MACRO(canMovePack) && {backpack (_this select 0) != '' || {(_this select 0) call MACRO(chestpack) != ''}});
                //"(_this select 0) call canMovePack && {backpack (_this select 0) != '' || {(_this select 0) call chestpack != ''}}"
    //based on this advExample I can assume that '' is resolved after all surrounding(or all completly) macros are resolved, so if it was inside a quoting macro it will become part of a string and be ignored



    double= "substring"; //"substring"
    double = "Here is a ""substring"""; //"Here is a ""substring"""
    double = "Here is a """; //"Here is a """
    double = """"; //""""
//    double = """""; //invalid
//    double = """; //invalid
    double = "Here is a MACRO(substring)"; //"Here is a MACRO(substring)"
    double = "Here is a 'substring'"; //"Here is a 'substring'"

    single = 'substring'; //"'substring'"
    single = 'Here is a "substring"'; //"'Here is a ""substring""'"
    single = 'Here is a ""substring""'; //"'Here is a """"substring""""'"
    single = 'Here is a """substring"""'; //"'Here is a """"substring""""'"
    single = 'Here is a 'substring''; //"'Here is a 'substring''"
    single = 'Here is a ''substring'''; //"'Here is a ''substring'''"
    single = 'Here is a '''substring''''; //"'Here is a '''substring''''"
    single = 'Here is a MACRO(substring)'; //"'Here is a substring'"
    single = 'Here is a SQ(substring)'; //"'Here is a ""substring""'"
    single = 'Here is a DQ(substring)'; //"'Here is a """"substring""""'"
    single = ' ''' '; //"' ''' '"
    single = ' '' ';  //"' '' '"
    single = '''; //"'''"

// '' will be encased with "" but keep the original '' and it will convert any " it finds into "", but this happens as a thing in preprocessor (after macros) and as such inside of '' checks for strings "" still happen and anything inside those strings will be basicly ignored, so if a invalid string appear in there like """ it will fail/error. What I mean is preprocessor will skip strings. The bonus of '' is that it allows for use of macros, since they are processed before '' (or maybe the processing does like an inside to outside thing) so any macros inside will be resolved. It also acts weird in the sense that it takes the firstmost and lastmost ' instead of parsing left to right kinda, by that i mean that there can be a single ' inside of '' and it will basicly get skipped/included in the bigger '', as if the process checks leftmost ' in the line and rightmost ' in the line and anything inbetween them is included?


    macro = SQ(substring); //"substring"
    macro = SQ(Here is a "substring"); //invalid
    macro = SQ(Here is a ""substring""); //"Here is a ""substring"""
    macro = SQ(Here is a ""); //"Here is a """
    macro = SQ(Here is a MACRO(substring)); //"Here is a substring"
    macro = SQ(Here is a SQ(substring)); //invalid
    macro = SQ(Here is a DQ(substring)); //"Here is a ""substring"""
    macro = SQ(Here is a 'substring'); //"Here is a 'substring'"
    macro = MACRO(''); //"''"
    macro = MACRO(""); //""


    test = SQ(DQ(substring)); //"""substring""";
    test = '"""'; //invalid
    test = '""'; //"'""""'"

    macro = SQ('substring'); //"'substring'"
    macro = SQ(Here is a 'substring'); //"Here is a 'substring'"
    macro = SQ('""'); //"'""'"
    macro = SQ('Here is a "substring"'); //invalid
    macro = SQ('Here is a ""substring""'); //invalid
    macro = SQ("substring"); //invalid
    macro = SQ(Here is a "substring"); //invalid
    macro = SQ("Here is a 'substring'"); //invalid
    macro = SQ("Here is a SQ(substring)"); //invalid
    macro = SQ('Here is a SQ(substring)'); //invalid
    macro = SQ(SQ(substring)); //invalid
    macro = SQ('SQ(substring)'); //invalid
    macro = SQ('SQ()'); //invalid
    macro = DQ(substring); //invalid


//ok weirdest black magic ever:
//    test = MACRO(') and MACRO(')); //"' and ')"
//    test = MACRO(') and 'MACRO(')); //"' and '')"


//Other Testing
//    test = "test " + "test"; //invalid

//on a separate topic, any sort of multiline strings are invalid apparently, but something else is weird

/* invalid
    double = "multi
              line";
*/

/* invalid
    single = 'multi
              line';
*/

/* invalid
    macro = SQ(multi
        line);
*/

};
