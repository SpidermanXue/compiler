/*
 * Generated Wed Mar 02 10:44:41 PST 2016
 */

	.section	".rodata" 
	.align  	4 
.$$.intFmt:
	.asciz  	"%d"
.$$.strFmt:
	.asciz  	"%s"
.$$.strTF:
	.asciz  	"false\0\0\0true"
.$$.strEndl:
	.asciz  	"\n"
.$$.strArrBound:
	.asciz  	"Index value of %d is outside legal range [0, %d).\n "
.$$.strNullPtr:
	.asciz  	"Attempt to dereference NULL pointer.\n  "

	.section	".text" 
	.align  	4 
.$$.printBool:
	save    	%sp, -96, %sp
	set     	.$$.strTF, %o0
	cmp     	%g0, %i0
	be      	.$$.printBool2
	nop     
	add     	%o0, 8, %o0
.$$.printBool2:
	call    	printf
	nop     
	ret     
	restore 

.$$.arrCheck:
	save    	%sp, -96, %sp
	cmp     	%i0, %g0
	bl      	.$$.arrCheck2
	nop     
	cmp     	%i0, %i1
	bge     	.$$.arrCheck2
	nop     
	ret     
	restore 
.$$.arrCheck2:
	set     	.$$.strArrBound, %o0
	mov     	%i0, %o1
	call    	printf
	mov     	%i1, %o2
	call    	exit
	mov     	1, %o0
	ret     
	restore 

.$$.ptrCheck:
	save    	%sp, -96, %sp
	cmp     	%i0, %g0
	bne     	.$$.ptrCheck2
	nop     
	set     	.$$.strNullPtr, %o0
	call    	printf
	nop     
	call    	exit
	mov     	1, %o0
.$$.ptrCheck2:
	ret     
	restore 


	.section	".data" 
	.align  	4 
	.global 	c 
c:
	.word   	10 
	
	.section	".text" 
	.align  	4 

	.section	".bss" 
	.align  	4 
	.global 	f 
f:
	.skip   	40 
	
	.section	".text" 
	.align  	4 

	.section	".bss" 
	.align  	4 
	.global 	i 
i:
	.skip   	40 
	
	.section	".text" 
	.align  	4 

	.section	".bss" 
	.align  	4 
	.global 	b 
b:
	.skip   	40 
	
	.section	".text" 
	.align  	4 
	.global 	main
main:
main.void:
	set     	SAVE.main.void, %g1
	save    	%sp, %g1, %sp
	
		! Store params
		
		! i=0
		set     	-4, %o1
		add     	%fp, %o1, %o1
		set     	0, %o0
		st      	%o0, [%o1]
		
		! while ( ... ) 
	.$$.loopCheck.1:	
			
			!i Operator c
			set     	-4, %l7
			add     	%fp, %l7, %l7
			ld      	[%l7], %o0
			set     	c, %l7
			add     	%g0, %l7, %l7
			ld      	[%l7], %o1
			cmp     	%o0, %o1
			bge     	.$$.cmp.1
			mov     	%g0, %o0
			inc     	%o0
		.$$.cmp.1:
			set     	-8, %o1
			add     	%fp, %o1, %o1
			st      	%o0, [%o1]
			
			! Check loop condition 
			set     	-8, %l7
			add     	%fp, %l7, %l7
			ld      	[%l7], %o0
			cmp     	%o0, %g0
			be      	.$$.loopEnd.1
			nop     

			! Start of loop body				
				
				! print VarSTO@4b1210ee
				set     	, %l7
				add     	%fp, %l7, %l7
				ld      	[%l7], %l7
				ld      	[%l7], %f0
				call    	printFloat
				nop     
				
				! print VarSTO@4d7e1886
				set     	, %l7
				add     	%fp, %l7, %l7
				ld      	[%l7], %o1
				set     	.$$.intFmt, %o0
				call    	printf
				nop     
				
				! print VarSTO@3cd1a2f1
				set     	, %l7
				add     	%fp, %l7, %l7
				ld      	[%l7], %l7
				ld      	[%l7], %o0
				call    	.$$.printBool
				nop     
				

				! cout << endl 
				set     	.$$.strEndl, %o0
				call    	printf
				nop     
				
				! ++i
				set     	-4, %l7
				add     	%fp, %l7, %l7
				ld      	[%l7], %o0
				set     	1, %o1
				add     	%o0, %o1, %o2
				set     	-12, %o1
				add     	%fp, %o1, %o1
				st      	%o2, [%o1]
				set     	-4, %o1
				add     	%fp, %o1, %o1
				st      	%o2, [%o1]
				
			!End of loop body 
			ba      	.$$.loopCheck.1
			nop     
		.$$.loopEnd.1:
		
		! return0;
		set     	0, %i0
		call    	main.void.fini 
		nop
		ret
		restore
	
	! End of function main.void
	call    	main.void.fini 
	nop
	ret
	restore
	SAVE.main.void = -(92 + 12) & -8 

main.void.fini:
	save    	%sp, -96, %sp
	ret
	restore
