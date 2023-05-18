global:
	n = 1000
	firstTerm = 0

main:
	secondTerm = 1
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
	ret 0


