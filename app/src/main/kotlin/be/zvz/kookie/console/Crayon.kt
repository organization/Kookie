/**
 * MIT License
 *
 * Copyright (c) Jaewe Heo <jaeweheo@gmail.com> (importre.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package be.zvz.kookie.console

fun String.bold() = "\u001b[1m${this}\u001b[0m"
fun String.italic() = "\u001b[3m${this}\u001b[0m"
fun String.underline() = "\u001b[4m${this}\u001b[0m"
fun String.reversed() = "\u001b[7m${this}\u001b[0m"
fun String.black() = "\u001b[30m${this}\u001b[0m"
fun String.blue() = "\u001b[34m${this}\u001b[0m"
fun String.cyan() = "\u001b[36m${this}\u001b[0m"
fun String.green() = "\u001b[32m${this}\u001b[0m"
fun String.magenta() = "\u001b[35m${this}\u001b[0m"
fun String.red() = "\u001b[31m${this}\u001b[0m"
fun String.white() = "\u001b[37m${this}\u001b[0m"
fun String.yellow() = "\u001b[33m${this}\u001b[0m"
fun String.brightBlack() = "\u001b[30;1m${this}\u001b[0m"
fun String.brightBlue() = "\u001b[34;1m${this}\u001b[0m"
fun String.brightCyan() = "\u001b[36;1m${this}\u001b[0m"
fun String.brightGreen() = "\u001b[32;1m${this}\u001b[0m"
fun String.brightMagenta() = "\u001b[35;1m${this}\u001b[0m"
fun String.brightRed() = "\u001b[31;1m${this}\u001b[0m"
fun String.brightWhite() = "\u001b[37;1m${this}\u001b[0m"
fun String.brightYellow() = "\u001b[33;1m${this}\u001b[0m"
fun String.bgBlack() = "\u001b[40m${this}\u001b[0m"
fun String.bgBlue() = "\u001b[44m${this}\u001b[0m"
fun String.bgCyan() = "\u001b[46m${this}\u001b[0m"
fun String.bgGreen() = "\u001b[42m${this}\u001b[0m"
fun String.bgMagenta() = "\u001b[45m${this}\u001b[0m"
fun String.bgRed() = "\u001b[41m${this}\u001b[0m"
fun String.bgWhite() = "\u001b[47m${this}\u001b[0m"
fun String.bgYellow() = "\u001b[43m${this}\u001b[0m"
fun String.bgBrightBlack() = "\u001b[40;1m${this}\u001b[0m"
fun String.bgBrightBlue() = "\u001b[44;1m${this}\u001b[0m"
fun String.bgBrightCyan() = "\u001b[46;1m${this}\u001b[0m"
fun String.bgBrightGreen() = "\u001b[42;1m${this}\u001b[0m"
fun String.bgBrightMagenta() = "\u001b[45;1m${this}\u001b[0m"
fun String.bgBrightRed() = "\u001b[41;1m${this}\u001b[0m"
fun String.bgBrightWhite() = "\u001b[47;1m${this}\u001b[0m"
fun String.bgBrightYellow() = "\u001b[43;1m${this}\u001b[0m"

fun Char.bold() = "\u001b[1m${this}\u001b[0m"
fun Char.italic() = "\u001b[3m${this}\u001b[0m"
fun Char.underline() = "\u001b[4m${this}\u001b[0m"
fun Char.reversed() = "\u001b[7m${this}\u001b[0m"
fun Char.black() = "\u001b[30m${this}\u001b[0m"
fun Char.blue() = "\u001b[34m${this}\u001b[0m"
fun Char.cyan() = "\u001b[36m${this}\u001b[0m"
fun Char.green() = "\u001b[32m${this}\u001b[0m"
fun Char.magenta() = "\u001b[35m${this}\u001b[0m"
fun Char.red() = "\u001b[31m${this}\u001b[0m"
fun Char.white() = "\u001b[37m${this}\u001b[0m"
fun Char.yellow() = "\u001b[33m${this}\u001b[0m"
fun Char.brightBlack() = "\u001b[30;1m${this}\u001b[0m"
fun Char.brightBlue() = "\u001b[34;1m${this}\u001b[0m"
fun Char.brightCyan() = "\u001b[36;1m${this}\u001b[0m"
fun Char.brightGreen() = "\u001b[32;1m${this}\u001b[0m"
fun Char.brightMagenta() = "\u001b[35;1m${this}\u001b[0m"
fun Char.brightRed() = "\u001b[31;1m${this}\u001b[0m"
fun Char.brightWhite() = "\u001b[37;1m${this}\u001b[0m"
fun Char.brightYellow() = "\u001b[33;1m${this}\u001b[0m"
fun Char.bgBlack() = "\u001b[40m${this}\u001b[0m"
fun Char.bgBlue() = "\u001b[44m${this}\u001b[0m"
fun Char.bgCyan() = "\u001b[46m${this}\u001b[0m"
fun Char.bgGreen() = "\u001b[42m${this}\u001b[0m"
fun Char.bgMagenta() = "\u001b[45m${this}\u001b[0m"
fun Char.bgRed() = "\u001b[41m${this}\u001b[0m"
fun Char.bgWhite() = "\u001b[47m${this}\u001b[0m"
fun Char.bgYellow() = "\u001b[43m${this}\u001b[0m"
fun Char.bgBrightBlack() = "\u001b[40;1m${this}\u001b[0m"
fun Char.bgBrightBlue() = "\u001b[44;1m${this}\u001b[0m"
fun Char.bgBrightCyan() = "\u001b[46;1m${this}\u001b[0m"
fun Char.bgBrightGreen() = "\u001b[42;1m${this}\u001b[0m"
fun Char.bgBrightMagenta() = "\u001b[45;1m${this}\u001b[0m"
fun Char.bgBrightRed() = "\u001b[41;1m${this}\u001b[0m"
fun Char.bgBrightWhite() = "\u001b[47;1m${this}\u001b[0m"
fun Char.bgBrightYellow() = "\u001b[43;1m${this}\u001b[0m"
