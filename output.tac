PRINT_INT:
	syscall 1
	ret 0

global:
	a = 0

main:
	savec
	addp 0 a
	call print
	loadc
	ret 0


