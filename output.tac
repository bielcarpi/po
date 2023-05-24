PRINT_INT:
	syscall 1
	ret 0

global:
	n = 10
	firstTerm = 0

main:
	savec
	addp 0 8
	addp 1 5
	addp 2 7
	call hola
	loadc
	savec
	call adeu
	loadc
	ret 0

hola:
	n = a
	secondTerm = 1
	i = 0
E0:
	if i >= n goto E1
	s0 = firstTerm + secondTerm
	nextTerm = s0
	firstTerm = secondTerm
	secondTerm = nextTerm
	i = i + 1
	goto E0
E1:
	savec
	addp 0 firstTerm
	call print
	loadc
	ret 0

adeu:
	n = n + 1
	n = n + 1
	ret 0


