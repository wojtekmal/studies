make rstack_example -B
echo

for test in zero one two three four five memory
do
    echo $test
    valgrind --leak-check=yes ./rstack_example $test

    diff file_$test.out correct_answers/file_$test.out
    if [ $? -eq 0 ]
    then
        echo CORRECT OUTPUT
    else
        echo WRONG OUTPUT
    fi

    echo
done