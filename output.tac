PRINT_INT:
	syscall 1
	ret 0

global:
	n = 15
	firstTerm = 0

main:
	secondTerm = 2
	i = 0
E0:
	if i >= n goto E1
	t9 = firstTerm + secondTerm
	nextTerm = t9
	firstTerm = secondTerm
	secondTerm = nextTerm
	i = i + 1
	goto E0
E1:
	addp 0 secondTerm
	call print
	ret 0


