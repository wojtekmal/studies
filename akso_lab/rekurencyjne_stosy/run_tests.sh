make rstack_example -B

for test in zero one two three four five memory
do
    echo $test
    ./rstack_example $test
    diff file_$test.out correct_answers/file_$test.out
    if [ $? -eq 0 ]
    then
        echo OK
    else
        echo WRONG OUTPUT
    fi
    echo
done