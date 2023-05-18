global:
	n = 10
	firstTerm = 0

main:
	secondTerm = 1
	i = 0
E0:
	if i >= n goto E1
	call abc
	call firstTerm
	t9 = firstTerm + secondTerm
	nextTerm = t9
	i = i + 1
	goto E0
E1:
	firstTerm = secondTerm
	secondTerm = nextTerm
	ret 0


