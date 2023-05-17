li $t9, 3sw $t9, a
li $t8, 4li $t7, 5mult$t9, $t8, $t7sw t9, $t9
li $t8, 3add$t9, $t8, t9sw t9, $t9
li $t9, t9sw $t9, a
li $t9, 1add$t9, $t8, bsw b, $t9
li $t8, 8li $t7, 5mult$t9, $t8, $t7sw t9, $t9
li $t9, t9sw $t9, c
li $t9, 1sub$t9, $t8, csw c, $t9
li $t8, 6li $t7, 2mult$t9, $t8, $t7sw t9, $t9
li $t9, t9sw $t9, a
null
li $t9, 4sw $t9, a
null
null
li $t9, 5sw $t9, a
null
null
li $t9, 1add$t9, $t8, csw c, $t9
li $t8, 8li $t7, 8mult$t9, $t8, $t7sw t9, $t9
null
li $t9, t9sw $t9, b
null
li $t9, 6sw $t9, a
li $t8, 3li $t7, 4mult$t9, $t8, $t7sw t9, $t9
li $t9, t9sw $t9, b
li $t9, 1add$t9, $t8, zsw z, $t9
