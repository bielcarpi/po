PRINT_INT:
	syscall 1
	a0 = 0xA
	syscall 11
	ret 0

READ_INT:
	syscall 5
	ret v0

global:
	a = 0

main:
	savec
	call read
	loadc
	a = v0
	savec
	addp 0 a
	call print
	loadc
	savec
	addp 0 "hollal"
	call prints
	loadc
	ret 0

PRINT_STR:
	syscall 4
	ret 0


