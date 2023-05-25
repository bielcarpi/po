PRINT_INT:
	syscall 1
	a0 = 0xA
	syscall 11
	ret 0

recFactorial:
	savec
	call num
	loadc
	s0 = num * recFactorial
	s4 = s0
	ret s4
	ret 1

global:

main:
	num = 3
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
	addp 0 factorial
	call print
	loadc
	ret 0


