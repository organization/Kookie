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

fun String.raw(): String {
    val regex = Regex("\u001B(\\[\\d+(?:;1)?m.+?)\u001B(\\[0m)")
    return replace(regex, "\\\\u001b$1\\\\u001b$2")
}

const val reset = "\u001b[0m"
const val bold = "\u001b[1m"
const val italic = "\u001b[3m"
const val underline = "\u001b[4m"
const val reversed = "\u001b[7m"
const val black = "\u001b[30m"
const val blue = "\u001b[34m"
const val cyan = "\u001b[36m"
const val green = "\u001b[32m"
const val magenta = "\u001b[35m"
const val red = "\u001b[31m"
const val white = "\u001b[37m"
const val yellow = "\u001b[33m"
const val brightBlack = "\u001b[30;1m"
const val brightBlue = "\u001b[34;1m"
const val brightCyan = "\u001b[36;1m"
const val brightGreen = "\u001b[32;1m"
const val brightMagenta = "\u001b[35;1m"
const val brightRed = "\u001b[31;1m"
const val brightWhite = "\u001b[37;1m"
const val brightYellow = "\u001b[33;1m"
const val bgBlack = "\u001b[40m"
const val bgBlue = "\u001b[44m"
const val bgCyan = "\u001b[46m"
const val bgGreen = "\u001b[42m"
const val bgMagenta = "\u001b[45m"
const val bgRed = "\u001b[41m"
const val bgWhite = "\u001b[47m"
const val bgYellow = "\u001b[43m"
const val bgBrightBlack = "\u001b[40;1m"
const val bgBrightBlue = "\u001b[44;1m"
const val bgBrightCyan = "\u001b[46;1m"
const val bgBrightGreen = "\u001b[42;1m"
const val bgBrightMagenta = "\u001b[45;1m"
const val bgBrightRed = "\u001b[41;1m"
const val bgBrightWhite = "\u001b[47;1m"
const val bgBrightYellow = "\u001b[43;1m"
