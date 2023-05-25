PRINT_INT:
	syscall 1
	a0 = 0xA
	syscall 11
	ret 0

READ_INT:
	syscall 5
	ret v0

global:
	b = 0

main:
	savec
	addp 0 $z1101
	call prints
	loadc
	savec
	call read
	loadc
	b = v0
	savec
	addp 0 $z1102
	call prints
	loadc
	s0 = b * 2
	b = s0
	savec
	addp 0 b
	call print
	loadc
	ret $z1103

PRINT_STR:
	syscall 4
	ret 0


