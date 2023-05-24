PRINT_INT:
	syscall 1
	ret 0

global:
	num1 = 5
	num2 = 2
	option = 10

main:
	aux = 0
	if option == 0 goto E1
	if option == 1 goto E2
	if option == 2 goto E3
	goto E4
E1:
	savec
	addp 0 num2
	call print
	loadc
	goto E0
E2:
	savec
	addp 0 num1
	call print
	loadc
	goto E0
E3:
	s0 = num1 + num2
	aux = s0
	savec
	addp 0 aux
	call print
	loadc
	goto E0
E4:
	savec
	addp 0 aux
	call print
	loadc
E0:
	ret 0


