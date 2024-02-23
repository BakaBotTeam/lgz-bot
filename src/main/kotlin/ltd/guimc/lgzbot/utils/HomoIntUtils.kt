package ltd.guimc.lgzbot.utils

import huzpsb.ll4j.utils.random.NRandom
import kotlin.math.pow
import kotlin.math.sqrt

object HomoIntUtils {
    val nRandom = NRandom(System.currentTimeMillis())

    fun getInt(num: Long): String {
        if (num == 229028L) return "114514+114514"
        if (num == 114514L) return "114514"
        if (num == 58596L) return "114*514"
        if (num == 49654L) return "11*4514"
        if (num == 45804L) return "11451*4"
        if (num == 23256L) return "114*51*4"
        if (num == 22616L) return "11*4*514"
        if (num == 19844L) return "11*451*4"
        if (num == 16030L) return "1145*14"
        if (num == 14515L) return "1+14514"
        if (num == 14514L) return "1*14514"
        if (num == 14513L) return "-1+14514"
        if (num == 11455L) return "11451+4"
        if (num == 11447L) return "11451-4"
        if (num == 9028L) return "(1+1)*4514"
        if (num == 8976L) return "11*4*51*4"
        if (num == 7980L) return "114*5*14"
        if (num == 7710L) return "(1+14)*514"
        if (num == 7197L) return "1+14*514"
        if (num == 7196L) return "1*14*514"
        if (num == 7195L) return "-1+14*514"
        if (num == 6930L) return "11*45*14"
        if (num == 6682L) return "(1-14)*-514"
        if (num == 6270L) return "114*(51+4)"
        if (num == 5818L) return "114*51+4"
        if (num == 5810L) return "114*51-4"
        if (num == 5808L) return "(1+1451)*4"
        if (num == 5805L) return "1+1451*4"
        if (num == 5804L) return "1*1451*4"
        if (num == 5803L) return "-1+1451*4"
        if (num == 5800L) return "(1-1451)*-4"
        if (num == 5725L) return "1145*(1+4)"
        if (num == 5698L) return "11*(4+514)"
        if (num == 5610L) return "-11*(4-514)"
        if (num == 5358L) return "114*(51-4)"
        if (num == 5005L) return "11*(451+4)"
        if (num == 4965L) return "11*451+4"
        if (num == 4957L) return "11*451-4"
        if (num == 4917L) return "11*(451-4)"
        if (num == 4584L) return "(1145+1)*4"
        if (num == 4580L) return "1145*1*4"
        if (num == 4576L) return "(1145-1)*4"
        if (num == 4525L) return "11+4514"
        if (num == 4516L) return "1+1+4514"
        if (num == 4515L) return "1+1*4514"
        if (num == 4514L) return "1-1+4514"
        if (num == 4513L) return "-1*1+4514"
        if (num == 4512L) return "-1-1+4514"
        if (num == 4503L) return "-11+4514"
        if (num == 4112L) return "(1+1)*4*514"
        if (num == 3608L) return "(1+1)*451*4"
        if (num == 3598L) return "(11-4)*514"
        if (num == 3435L) return "-1145*(1-4)"
        if (num == 3080L) return "11*4*5*14"
        if (num == 3060L) return "(11+4)*51*4"
        if (num == 2857L) return "1+14*51*4"
        if (num == 2856L) return "1*14*51*4"
        if (num == 2855L) return "-1+14*51*4"
        if (num == 2850L) return "114*5*(1+4)"
        if (num == 2736L) return "114*(5+1)*4"
        if (num == 2652L) return "(1-14)*51*-4"
        if (num == 2570L) return "1*(1+4)*514"
        if (num == 2475L) return "11*45*(1+4)"
        if (num == 2420L) return "11*4*(51+4)"
        if (num == 2280L) return "114*5*1*4"
        if (num == 2248L) return "11*4*51+4"
        if (num == 2240L) return "11*4*51-4"
        if (num == 2166L) return "114*(5+14)"
        if (num == 2068L) return "11*4*(51-4)"
        if (num == 2067L) return "11+4*514"
        if (num == 2058L) return "1+1+4*514"
        if (num == 2057L) return "1/1+4*514"
        if (num == 2056L) return "1/1*4*514"
        if (num == 2055L) return "-1/1+4*514"
        if (num == 2054L) return "-1-1+4*514"
        if (num == 2045L) return "-11+4*514"
        if (num == 2044L) return "(1+145)*14"
        if (num == 2031L) return "1+145*14"
        if (num == 2030L) return "1*145*14"
        if (num == 2029L) return "-1+145*14"
        if (num == 2024L) return "11*(45+1)*4"
        if (num == 2016L) return "-(1-145)*14"
        if (num == 1980L) return "11*45*1*4"
        if (num == 1936L) return "11*(45-1)*4"
        if (num == 1848L) return "(11+451)*4"
        if (num == 1824L) return "114*(5-1)*4"
        if (num == 1815L) return "11+451*4"
        if (num == 1808L) return "1*(1+451)*4"
        if (num == 1806L) return "1+1+451*4"
        if (num == 1805L) return "1+1*451*4"
        if (num == 1804L) return "1-1+451*4"
        if (num == 1803L) return "1*-1+451*4"
        if (num == 1802L) return "-1-1+451*4"
        if (num == 1800L) return "1*-(1-451)*4"
        if (num == 1793L) return "-11+451*4"
        if (num == 1760L) return "-(11-451)*4"
        if (num == 1710L) return "114*-5*(1-4)"
        if (num == 1666L) return "(114+5)*14"
        if (num == 1632L) return "(1+1)*4*51*4"
        if (num == 1542L) return "1*-(1-4)*514"
        if (num == 1526L) return "(114-5)*14"
        if (num == 1485L) return "11*-45*(1-4)"
        if (num == 1456L) return "1+1451+4"
        if (num == 1455L) return "1*1451+4"
        if (num == 1454L) return "-1+1451+4"
        if (num == 1448L) return "1+1451-4"
        if (num == 1447L) return "1*1451-4"
        if (num == 1446L) return "-1+1451-4"
        if (num == 1428L) return "(11-4)*51*4"
        if (num == 1386L) return "11*(4+5)*14"
        if (num == 1260L) return "(1+1)*45*14"
        if (num == 1159L) return "1145+14"
        if (num == 1150L) return "1145+1+4"
        if (num == 1149L) return "1145+1*4"
        if (num == 1148L) return "1145-1+4"
        if (num == 1142L) return "1145+1-4"
        if (num == 1141L) return "1145-1*4"
        if (num == 1140L) return "(1145-1)-4"
        if (num == 1131L) return "1145-14"
        if (num == 1100L) return "11*4*5*(1+4)"
        if (num == 1056L) return "11*4*(5+1)*4"
        if (num == 1050L) return "(11+4)*5*14"
        if (num == 1036L) return "(1+1)*(4+514)"
        if (num == 1026L) return "114*-(5-14)"
        if (num == 1020L) return "1*(1+4)*51*4"
        if (num == 981L) return "1+14*5*14"
        if (num == 980L) return "1*14*5*14"
        if (num == 979L) return "-1+14*5*14"
        if (num == 910L) return "-(1-14)*5*14"
        if (num == 906L) return "(1+1)*451+4"
        if (num == 898L) return "(1+1)*451-4"
        if (num == 894L) return "(1+1)*(451-4)"
        if (num == 880L) return "11*4*5*1*4"
        if (num == 836L) return "11*4*(5+14)"
        if (num == 827L) return "11+4*51*4"
        if (num == 825L) return "(11+4)*(51+4)"
        if (num == 818L) return "1+1+4*51*4"
        if (num == 817L) return "1*1+4*51*4"
        if (num == 816L) return "1*1*4*51*4"
        if (num == 815L) return "-1+1*4*51*4"
        if (num == 814L) return "-1-1+4*51*4"
        if (num == 805L) return "-11+4*51*4"
        if (num == 784L) return "(11+45)*14"
        if (num == 771L) return "1+14*(51+4)"
        if (num == 770L) return "1*14*(51+4)"
        if (num == 769L) return "(11+4)*51+4"
        if (num == 761L) return "(1+14)*51-4"
        if (num == 730L) return "(1+145)*(1+4)"
        if (num == 726L) return "1+145*(1+4)"
        if (num == 725L) return "1*145*(1+4)"
        if (num == 724L) return "-1-145*-(1+4)"
        if (num == 720L) return "(1-145)*-(1+4)"
        if (num == 719L) return "1+14*51+4"
        if (num == 718L) return "1*14*51+4"
        if (num == 717L) return "-1-14*-51+4"
        if (num == 715L) return "(1-14)*-(51+4)"
        if (num == 711L) return "1+14*51-4"
        if (num == 710L) return "1*14*51-4"
        if (num == 709L) return "-1+14*51-4"
        if (num == 705L) return "(1+14)*(51-4)"
        if (num == 704L) return "11*4*(5-1)*4"
        if (num == 688L) return "114*(5+1)+4"
        if (num == 680L) return "114*(5+1)-4"
        if (num == 667L) return "-(1-14)*51+4"
        if (num == 660L) return "(114+51)*4"
        if (num == 659L) return "1+14*(51-4)"
        if (num == 658L) return "1*14*(51-4)"
        if (num == 657L) return "-1+14*(51-4)"
        if (num == 649L) return "11*(45+14)"
        if (num == 644L) return "1*(1+45)*14"
        if (num == 641L) return "11+45*14"
        if (num == 632L) return "1+1+45*14"
        if (num == 631L) return "1*1+45*14"
        if (num == 630L) return "1*1*45*14"
        if (num == 629L) return "1*-1+45*14"
        if (num == 628L) return "114+514"
        if (num == 619L) return "-11+45*14"
        if (num == 616L) return "1*-(1-45)*14"
        if (num == 612L) return "-1*(1-4)*51*4"
        if (num == 611L) return "(1-14)*-(51-4)"
        if (num == 609L) return "11*(4+51)+4"
        if (num == 601L) return "11*(4+51)-4"
        if (num == 595L) return "(114+5)*(1+4)"
        if (num == 584L) return "114*5+14"
        if (num == 581L) return "1+145*1*4"
        if (num == 580L) return "1*145/1*4"
        if (num == 579L) return "-1+145*1*4"
        if (num == 576L) return "1*(145-1)*4"
        if (num == 575L) return "114*5+1+4"
        if (num == 574L) return "114*5/1+4"
        if (num == 573L) return "114*5-1+4"
        if (num == 567L) return "114*5+1-4"
        if (num == 566L) return "114*5*1-4"
        if (num == 565L) return "114*5-1-4"
        if (num == 561L) return "11/4*51*4"
        if (num == 560L) return "(1+1)*4*5*14"
        if (num == 558L) return "11*4+514"
        if (num == 556L) return "114*5-14"
        if (num == 545L) return "(114-5)*(1+4)"
        if (num == 529L) return "1+14+514"
        if (num == 528L) return "1*14+514"
        if (num == 527L) return "-1+14+514"
        if (num == 522L) return "(1+1)*4+514"
        if (num == 521L) return "11-4+514"
        if (num == 520L) return "1+1+4+514"
        if (num == 519L) return "1+1*4+514"
        if (num == 518L) return "1-1+4+514"
        if (num == 517L) return "-1+1*4+514"
        if (num == 516L) return "-1-1+4+514"
        if (num == 514L) return "(1-1)/4+514"
        if (num == 513L) return "-11*(4-51)-4"
        if (num == 512L) return "1+1-4+514"
        if (num == 511L) return "1*1-4+514"
        if (num == 510L) return "1-1-4+514"
        if (num == 509L) return "11*45+14"
        if (num == 508L) return "-1-1-4+514"
        if (num == 507L) return "-11+4+514"
        if (num == 506L) return "-(1+1)*4+514"
        if (num == 502L) return "11*(45+1)-4"
        if (num == 501L) return "1-14+514"
        if (num == 500L) return "11*45+1+4"
        if (num == 499L) return "11*45*1+4"
        if (num == 498L) return "11*45-1+4"
        if (num == 495L) return "11*(4+5)*(1+4)"
        if (num == 492L) return "11*45+1-4"
        if (num == 491L) return "11*45-1*4"
        if (num == 490L) return "11*45-1-4"
        if (num == 488L) return "11*(45-1)+4"
        if (num == 481L) return "11*45-14"
        if (num == 480L) return "11*(45-1)-4"
        if (num == 476L) return "(114+5)/1*4"
        if (num == 470L) return "-11*4+514"
        if (num == 466L) return "11+451+4"
        if (num == 460L) return "114*(5-1)+4"
        if (num == 458L) return "11+451-4"
        if (num == 457L) return "1+1+451+4"
        if (num == 456L) return "1*1+451+4"
        if (num == 455L) return "1-1+451+4"
        if (num == 454L) return "-1+1*451+4"
        if (num == 453L) return "-1-1+451+4"
        if (num == 452L) return "114*(5-1)-4"
        if (num == 450L) return "(1+1)*45*(1+4)"
        if (num == 449L) return "1+1+451-4"
        if (num == 448L) return "1+1*451-4"
        if (num == 447L) return "1/1*451-4"
        if (num == 446L) return "1*-1+451-4"
        if (num == 445L) return "-1-1+451-4"
        if (num == 444L) return "-11+451+4"
        if (num == 440L) return "(1+1)*4*(51+4)"
        if (num == 438L) return "(1+145)*-(1-4)"
        if (num == 436L) return "-11+451-4"
        if (num == 435L) return "-1*145*(1-4)"
        if (num == 434L) return "-1-145*(1-4)"
        if (num == 432L) return "(1-145)*(1-4)"
        if (num == 412L) return "(1+1)*4*51+4"
        if (num == 404L) return "(1+1)*4*51-4"
        if (num == 400L) return "-114+514"
        if (num == 396L) return "11*4*-(5-14)"
        if (num == 385L) return "(11-4)*(51+4)"
        if (num == 376L) return "(1+1)*4*(51-4)"
        if (num == 375L) return "(1+14)*5*(1+4)"
        if (num == 368L) return "(1+1)*(45+1)*4"
        if (num == 363L) return "(1+1451)/4"
        if (num == 361L) return "(11-4)*51+4"
        if (num == 360L) return "(1+1)*45*1*4"
        if (num == 357L) return "(114+5)*-(1-4)"
        if (num == 353L) return "(11-4)*51-4"
        if (num == 352L) return "(1+1)*(45-1)*4"
        if (num == 351L) return "1+14*-5*-(1+4)"
        if (num == 350L) return "1*(1+4)*5*14"
        if (num == 349L) return "-1+14*5*(1+4)"
        if (num == 341L) return "11*(45-14)"
        if (num == 337L) return "1-14*-(5+1)*4"
        if (num == 336L) return "1*14*(5+1)*4"
        if (num == 335L) return "-1+14*(5+1)*4"
        if (num == 329L) return "(11-4)*(51-4)"
        if (num == 327L) return "-(114-5)*(1-4)"
        if (num == 325L) return "-(1-14)*5*(1+4)"
        if (num == 318L) return "114+51*4"
        if (num == 312L) return "(1-14)*-(5+1)*4"
        if (num == 300L) return "(11+4)*5/1*4"
        if (num == 297L) return "-11*(4+5)*(1-4)"
        if (num == 291L) return "11+4*5*14"
        if (num == 286L) return "(1145-1)/4"
        if (num == 285L) return "(11+4)*(5+14)"
        if (num == 282L) return "1+1+4*5*14"
        if (num == 281L) return "1+14*5/1*4"
        if (num == 280L) return "1-1+4*5*14"
        if (num == 279L) return "1*-1+4*5*14"
        if (num == 278L) return "-1-1+4*5*14"
        if (num == 275L) return "1*(1+4)*(51+4)"
        if (num == 270L) return "(1+1)*45*-(1-4)"
        if (num == 269L) return "-11+4*5*14"
        if (num == 268L) return "11*4*(5+1)+4"
        if (num == 267L) return "1+14*(5+14)"
        if (num == 266L) return "1*14*(5+14)"
        if (num == 265L) return "-1+14*(5+14)"
        if (num == 260L) return "1*(14+51)*4"
        if (num == 259L) return "1*(1+4)*51+4"
        if (num == 257L) return "(1+1)/4*514"
        if (num == 252L) return "(114-51)*4"
        if (num == 251L) return "1*-(1+4)*-51-4"
        if (num == 248L) return "11*4+51*4"
        if (num == 247L) return "-(1-14)*(5+14)"
        if (num == 240L) return "(11+4)*(5-1)*4"
        if (num == 236L) return "11+45*(1+4)"
        if (num == 235L) return "1*(1+4)*(51-4)"
        if (num == 234L) return "11*4*5+14"
        if (num == 231L) return "11+4*(51+4)"
        if (num == 230L) return "1*(1+45)*(1+4)"
        if (num == 229L) return "1145/(1+4)"
        if (num == 227L) return "1+1+45*(1+4)"
        if (num == 226L) return "1*1+45*(1+4)"
        if (num == 225L) return "11*4*5+1+4"
        if (num == 224L) return "11*4*5/1+4"
        if (num == 223L) return "11*4*5-1+4"
        if (num == 222L) return "1+1+4*(51+4)"
        if (num == 221L) return "1/1+4*(51+4)"
        if (num == 220L) return "1*1*(4+51)*4"
        if (num == 219L) return "1+14+51*4"
        if (num == 218L) return "1*14+51*4"
        if (num == 217L) return "11*4*5+1-4"
        if (num == 216L) return "11*4*5-1*4"
        if (num == 215L) return "11*4*5-1-4"
        if (num == 214L) return "-11+45*(1+4)"
        if (num == 212L) return "(1+1)*4+51*4"
        if (num == 211L) return "11-4+51*4"
        if (num == 210L) return "1+1+4+51*4"
        if (num == 209L) return "1+1*4*51+4"
        if (num == 208L) return "1*1*4+51*4"
        if (num == 207L) return "-1+1*4*51+4"
        if (num == 206L) return "11*4*5-14"
        if (num == 204L) return "(1-1)/4+51*4"
        if (num == 202L) return "1+1-4+51*4"
        if (num == 201L) return "1/1-4+51*4"
        if (num == 200L) return "1/1*4*51-4"
        if (num == 199L) return "1*-1+4*51-4"
        if (num == 198L) return "-1-1+4*51-4"
        if (num == 197L) return "-11+4+51*4"
        if (num == 196L) return "-(1+1)*4+51*4"
        if (num == 195L) return "(1-14)*5*(1-4)"
        if (num == 192L) return "(1+1)*4*(5+1)*4"
        if (num == 191L) return "1-14+51*4"
        if (num == 190L) return "1*-14+51*4"
        if (num == 189L) return "-11-4+51*4"
        if (num == 188L) return "1-1-(4-51)*4"
        if (num == 187L) return "1/-1+4*(51-4)"
        if (num == 186L) return "1+1+(45+1)*4"
        if (num == 185L) return "1-1*-(45+1)*4"
        if (num == 184L) return "114+5*14"
        if (num == 183L) return "-1+1*(45+1)*4"
        if (num == 182L) return "1+1+45/1*4"
        if (num == 181L) return "1+1*45*1*4"
        if (num == 180L) return "1*1*45*1*4"
        if (num == 179L) return "-1/1+45*1*4"
        if (num == 178L) return "-1-1+45*1*4"
        if (num == 177L) return "1+1*(45-1)*4"
        if (num == 176L) return "1*1*(45-1)*4"
        if (num == 175L) return "-1+1*(45-1)*4"
        if (num == 174L) return "-1-1+(45-1)*4"
        if (num == 172L) return "11*4*(5-1)-4"
        if (num == 171L) return "114*(5+1)/4"
        if (num == 170L) return "(11-45)*-(1+4)"
        if (num == 169L) return "114+51+4"
        if (num == 168L) return "(11+45)*-(1-4)"
        if (num == 165L) return "11*-45/(1-4)"
        if (num == 161L) return "114+51-4"
        if (num == 160L) return "1+145+14"
        if (num == 159L) return "1*145+14"
        if (num == 158L) return "-1+145+14"
        if (num == 157L) return "1*(1-4)*-51+4"
        if (num == 154L) return "11*(4-5)*-14"
        if (num == 152L) return "(1+1)*4*(5+14)"
        if (num == 151L) return "1+145+1+4"
        if (num == 150L) return "1+145*1+4"
        if (num == 149L) return "1*145*1+4"
        if (num == 148L) return "1*145-1+4"
        if (num == 147L) return "-1+145-1+4"
        if (num == 146L) return "11+45*-(1-4)"
        if (num == 143L) return "1+145+1-4"
        if (num == 142L) return "1+145*1-4"
        if (num == 141L) return "1+145-1-4"
        if (num == 140L) return "1*145-1-4"
        if (num == 139L) return "-1+145-1-4"
        if (num == 138L) return "-1*(1+45)*(1-4)"
        if (num == 137L) return "1+1-45*(1-4)"
        if (num == 136L) return "1*1-45*(1-4)"
        if (num == 135L) return "-1/1*45*(1-4)"
        if (num == 134L) return "114+5/1*4"
        if (num == 133L) return "114+5+14"
        if (num == 132L) return "1+145-14"
        if (num == 131L) return "1*145-14"
        if (num == 130L) return "-1+145-14"
        if (num == 129L) return "114+5*-(1-4)"
        if (num == 128L) return "1+1+(4+5)*14"
        if (num == 127L) return "1-14*(5-14)"
        if (num == 126L) return "1*(14-5)*14"
        if (num == 125L) return "-1-14*(5-14)"
        if (num == 124L) return "114+5+1+4"
        if (num == 123L) return "114-5+14"
        if (num == 122L) return "114+5-1+4"
        if (num == 121L) return "11*(45-1)/4"
        if (num == 120L) return "-(1+1)*4*5*(1-4)"
        if (num == 118L) return "(1+1)*(45+14)"
        if (num == 117L) return "(1-14)*(5-14)"
        if (num == 116L) return "114+5+1-4"
        if (num == 115L) return "114+5*1-4"
        if (num == 114L) return "11*4+5*14"
        if (num == 113L) return "114-5/1+4"
        if (num == 112L) return "114-5-1+4"
        if (num == 111L) return "11+4*5*(1+4)"
        if (num == 110L) return "-(11-451)/4"
        if (num == 107L) return "11-4*-(5+1)*4"
        if (num == 106L) return "114-5+1-4"
        if (num == 105L) return "114+5-14"
        if (num == 104L) return "114-5-1-4"
        if (num == 103L) return "11*(4+5)+1*4"
        if (num == 102L) return "11*(4+5)-1+4"
        if (num == 101L) return "1+1*4*5*(1+4)"
        if (num == 100L) return "1*(1+4)*5*1*4"
        if (num == 99L) return "11*4+51+4"
        if (num == 98L) return "1+1+4*(5+1)*4"
        if (num == 97L) return "1+1*4*(5+1)*4"
        if (num == 96L) return "11*(4+5)+1-4"
        if (num == 95L) return "114-5-14"
        if (num == 94L) return "114-5/1*4"
        if (num == 93L) return "(1+1)*45-1+4"
        if (num == 92L) return "(1+1)*(45-1)+4"
        if (num == 91L) return "11*4+51-4"
        if (num == 90L) return "-114+51*4"
        if (num == 89L) return "(1+14)*5+14"
        if (num == 88L) return "1*14*(5+1)+4"
        if (num == 87L) return "11+4*(5+14)"
        if (num == 86L) return "(1+1)*45*1-4"
        if (num == 85L) return "1+14+5*14"
        if (num == 84L) return "1*14+5*14"
        if (num == 83L) return "-1+14+5*14"
        if (num == 82L) return "1+1+4*5/1*4"
        if (num == 81L) return "1/1+4*5*1*4"
        if (num == 80L) return "1-1+4*5*1*4"
        if (num == 79L) return "1*-1+4*5/1*4"
        if (num == 78L) return "(1+1)*4+5*14"
        if (num == 77L) return "11-4+5*14"
        if (num == 76L) return "1+1+4+5*14"
        if (num == 75L) return "1+14*5*1+4"
        if (num == 74L) return "1/1*4+5*14"
        if (num == 73L) return "1*14*5-1+4"
        if (num == 72L) return "-1-1+4+5*14"
        if (num == 71L) return "(1+14)*5-1*4"
        if (num == 70L) return "11+45+14"
        if (num == 69L) return "1*14+51+4"
        if (num == 68L) return "1+1-4+5*14"
        if (num == 67L) return "1-1*4+5*14"
        if (num == 66L) return "1*14*5-1*4"
        if (num == 65L) return "1*14*5-1-4"
        if (num == 64L) return "11*4+5*1*4"
        if (num == 63L) return "11*4+5+14"
        if (num == 62L) return "1+14+51-4"
        if (num == 61L) return "1+1+45+14"
        if (num == 60L) return "11+45*1+4"
        if (num == 59L) return "114-51-4"
        if (num == 58L) return "-1+1*45+14"
        if (num == 57L) return "1+14*5-14"
        if (num == 56L) return "1*14*5-14"
        if (num == 55L) return "-1+14*5-14"
        if (num == 54L) return "11-4+51-4"
        if (num == 53L) return "11+45+1-4"
        if (num == 52L) return "11+45/1-4"
        if (num == 51L) return "11+45-1-4"
        if (num == 50L) return "1+1*45/1+4"
        if (num == 49L) return "1*1*45/1+4"
        if (num == 48L) return "-11+45+14"
        if (num == 47L) return "1/-1+45-1+4"
        if (num == 46L) return "11*4+5+1-4"
        if (num == 45L) return "11+4*5+14"
        if (num == 44L) return "114-5*14"
        if (num == 43L) return "1+1*45+1-4"
        if (num == 42L) return "11+45-14"
        if (num == 41L) return "1/1*45*1-4"
        if (num == 40L) return "-11+4*51/4"
        if (num == 39L) return "-11+45+1+4"
        if (num == 38L) return "-11+45*1+4"
        if (num == 37L) return "-11+45-1+4"
        if (num == 36L) return "11+4*5+1+4"
        if (num == 35L) return "11*4+5-14"
        if (num == 34L) return "1-14+51-4"
        if (num == 33L) return "1+1+45-14"
        if (num == 32L) return "1*1+45-14"
        if (num == 31L) return "1/1*45-14"
        if (num == 30L) return "1*-1+45-14"
        if (num == 29L) return "-11+45-1-4"
        if (num == 28L) return "11+4*5+1-4"
        if (num == 27L) return "11+4*5/1-4"
        if (num == 26L) return "11-4+5+14"
        if (num == 25L) return "11*4-5-14"
        if (num == 24L) return "1+14-5+14"
        if (num == 23L) return "1*14-5+14"
        if (num == 22L) return "1*14+5-1+4"
        if (num == 21L) return "-1-1+4+5+14"
        if (num == 20L) return "-11+45-14"
        if (num == 19L) return "1+1+4*5+1-4"
        if (num == 18L) return "1+1+4*5*1-4"
        if (num == 17L) return "11+4*5-14"
        if (num == 16L) return "11-4-5+14"
        if (num == 15L) return "1+14-5+1+4"
        if (num == 14L) return "11+4-5/1+4"
        if (num == 13L) return "1*14-5/1+4"
        if (num == 12L) return "-11+4+5+14"
        if (num == 11L) return "11*-4+51+4"
        if (num == 10L) return "-11/4+51/4"
        if (num == 9L) return "11-4+5+1-4"
        if (num == 8L) return "11-4+5/1-4"
        if (num == 7L) return "11-4+5-1-4"
        if (num == 6L) return "1-14+5+14"
        if (num == 5L) return "11-4*5+14"
        if (num == 4L) return "-11-4+5+14"
        if (num == 3L) return "11*-4+51-4"
        if (num == 2L) return "-11+4-5+14"
        if (num == 1L) return "11/(45-1)*4"
        if (num == 0L) return "(1-1)*4514"
        if (num == -1L) return "11-4-5+1-4"

        var sum = ""
        val a = arrayListOf(
            229028,
            114514,
            58596,
            49654,
            45804,
            23256,
            22616,
            19844,
            16030,
            14515,
            14514,
            14513,
            11455,
            11447,
            9028,
            8976,
            7980,
            7710,
            7197,
            7196,
            7195,
            6930,
            6682,
            6270,
            5818,
            5810,
            5808,
            5805,
            5804,
            5803,
            5800,
            5725,
            5698,
            5610,
            5358,
            5005,
            4965,
            4957,
            4917,
            4584,
            4580,
            4576,
            4525,
            4516,
            4515,
            4514,
            4513,
            4512,
            4503,
            4112,
            3608,
            3598,
            3435,
            3080,
            3060,
            2857,
            2856,
            2855,
            2850,
            2736,
            2652,
            2570,
            2475,
            2420,
            2280,
            2248,
            2240,
            2166,
            2068,
            2067,
            2058,
            2057,
            2056,
            2055,
            2054,
            2045,
            2044,
            2031,
            2030,
            2029,
            2024,
            2016,
            1980,
            1936,
            1848,
            1824,
            1815,
            1808,
            1806,
            1805,
            1804,
            1803,
            1802,
            1800,
            1793,
            1760,
            1710,
            1666,
            1632,
            1542,
            1526,
            1485,
            1456,
            1455,
            1454,
            1448,
            1447,
            1446,
            1428,
            1386,
            1260,
            1159,
            1150,
            1149,
            1148,
            1142,
            1141,
            1140,
            1131,
            1100,
            1056,
            1050,
            1036,
            1026,
            1020,
            981,
            980,
            979,
            910,
            906,
            898,
            894,
            880,
            836,
            827,
            825,
            818,
            817,
            816,
            815,
            814,
            805,
            784,
            771,
            770,
            769,
            761,
            730,
            726,
            725,
            724,
            720,
            719,
            718,
            717,
            715,
            711,
            710,
            709,
            705,
            704,
            688,
            680,
            667,
            660,
            659,
            658,
            657,
            649,
            644,
            641,
            632,
            631,
            630,
            629,
            628,
            619,
            616,
            612,
            611,
            609,
            601,
            595,
            584,
            581,
            580,
            579,
            576,
            575,
            574,
            573,
            567,
            566,
            565,
            561,
            560,
            558,
            556,
            545,
            529,
            528,
            527,
            522,
            521,
            520,
            519,
            518,
            517,
            516,
            514,
            513,
            512,
            511,
            510,
            509,
            508,
            507,
            506,
            502,
            501,
            500,
            499,
            498,
            495,
            492,
            491,
            490,
            488,
            481,
            480,
            476,
            470,
            466,
            460,
            458,
            457,
            456,
            455,
            454,
            453,
            452,
            450,
            449,
            448,
            447,
            446,
            445,
            444,
            440,
            438,
            436,
            435,
            434,
            432,
            412,
            404,
            400,
            396,
            385,
            376,
            375,
            368,
            363,
            361,
            360,
            357,
            353,
            352,
            351,
            350,
            349,
            341,
            337,
            336,
            335,
            329,
            327,
            325,
            318,
            312,
            300,
            297,
            291,
            286,
            285,
            282,
            281,
            280,
            279,
            278,
            275,
            270,
            269,
            268,
            267,
            266,
            265,
            260,
            259,
            257,
            252,
            251,
            248,
            247,
            240,
            236,
            235,
            234,
            231,
            230,
            229,
            227,
            226,
            225,
            224,
            223,
            222,
            221,
            220,
            219,
            218,
            217,
            216,
            215,
            214,
            212,
            211,
            210,
            209,
            208,
            207,
            206,
            204,
            202,
            201,
            200,
            199,
            198,
            197,
            196,
            195,
            192,
            191,
            190,
            189,
            188,
            187,
            186,
            185,
            184,
            183,
            182,
            181,
            180,
            179,
            178,
            177,
            176,
            175,
            174,
            172,
            171,
            170,
            169,
            168,
            165,
            161,
            160,
            159,
            158,
            157,
            154,
            152,
            151,
            150,
            149,
            148,
            147,
            146,
            143,
            142,
            141,
            140,
            139,
            138,
            137,
            136,
            135,
            134,
            133,
            132,
            131,
            130,
            129,
            128,
            127,
            126,
            125,
            124,
            123,
            122,
            121,
            120,
            118,
            117,
            116,
            115,
            114,
            113,
            112,
            111,
            110,
            107,
            106,
            105,
            104,
            103,
            102,
            101,
            100,
            99,
            98,
            97,
            96,
            95,
            94,
            93,
            92,
            91,
            90,
            89,
            88,
            87,
            86,
            85,
            84,
            83,
            82,
            81,
            80,
            79,
            78,
            77,
            76,
            75,
            74,
            73,
            72,
            71,
            70,
            69,
            68,
            67,
            66,
            65,
            64,
            63,
            62,
            61,
            60,
            59,
            58,
            57,
            56,
            55,
            54,
            53,
            52,
            51,
            50,
            49,
            48,
            47,
            46,
            45,
            44,
            43,
            42,
            41,
            40,
            39,
            38,
            37,
            36,
            35,
            34,
            33,
            32,
            31,
            30,
            29,
            28,
            27,
            26,
            25,
            24,
            23,
            22,
            21,
            20,
            19,
            18,
            17,
            16,
            15,
            14,
            13,
            12,
            11,
            10,
            9,
            8,
            7,
            6,
            5,
            4,
            3,
            2,
            1,
            0,
            -1
        )

        if (num < 0) {
            return "(${getInt(-1)})*(${getInt((-1) * num)})"
        }

        var temp: Long
        var div: Long
        var m: Long

        if (num < 10000) //0~10000
        {
            var flag = 0
            var temp1 = 0
            for (i in 1..<num) {
                for (j in 0..<a.size) {
                    if (a[j].toLong() == i) {
                        flag = 1
                        temp1 = i.toInt()
                        break
                    }
                }
                if (flag == 1) {
                    temp1 = i.toInt()
                    break
                }
            }
            sum = "(" + getInt(temp1.toLong()) + ")+(" + getInt(num - temp1) + ")"
        } else if (num < 229028) //10001~229028
        {
            temp = nRandom.nextDouble(sqrt(num.toDouble()) - 100, sqrt(num.toDouble())).toLong()
            div = num / temp
            m = num % temp
            sum = if (div.toInt() != 1) "(" + getInt(div) + ")*(" + getInt(temp) + ")" //乘数为1
            else "(" + getInt(temp) + ")"
            if (m.toInt() != 0) sum += "+(" + getInt(m) + ")" //余数为0
        } else //大于229028
        {
            var total = 0
            var temp2 = num
            while (temp2 >= 229028) {
                temp2 /= 229028
                total++
                sum += "(" + getInt(229028) + ")*"
            }
            temp2 = (num / 229028.0.pow(total.toDouble())).toLong()
            if (temp2 != 1L) sum += "(${getInt(temp2)})" else sum = sum.substring(0, sum.length - 1)
            if (num.toDouble() != temp2 * 229028.0.pow(total.toDouble())) sum += "+(" + getInt(
                ((num - temp2 * 229028.0.pow(
                    total.toDouble()
                )).toLong())
            ) + ")"
        }

        return sum
    }
}