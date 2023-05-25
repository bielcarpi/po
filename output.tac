PRINT_INT:
	syscall 1
	a0 = 0xA
	syscall 11
	ret 0

recFactorial:
E0:
	if num < 1 goto E2
E1:
	savec
	s0 = num - 1
	s4 = s0
	addp 0 s4
	call recFactorial
	loadc
	aux = v0
	s0 = num * aux
	s4 = s0
	ret s4
	goto E3
E2:
	ret 1
E3:
E4:
	ret 0

READ_INT:
	syscall 5
	ret v0

global:

main:
	savec
	addp 0 $z1101
	call prints
	loadc
	savec
	addp 0 $z1102
	call prints
	loadc
	savec
	call read
	loadc
	num = v0
E5:
	if num > 12 goto E7
E6:
	savec
	addp 0 $z1103
	call prints
	loadc
	savec
	addp 0 num
	call print
	loadc
	savec
	addp 0 num
	call recFactorial
	loadc
	factorial = v0
	savec
	addp 0 $z1104
	call prints
	loadc
	savec
	addp 0 factorial
	call print
	loadc
	goto E8
E7:
	savec
	addp 0 $z1105
	call prints
	loadc
E8:
E9:
	ret 0

PRINT_STR:
	syscall 4
	ret 0


