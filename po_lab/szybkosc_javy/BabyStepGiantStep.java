public class BabyStepGiantStep {
    Long discreet_log(Long num, Long base, Long mod)
    {
        // Find the smallest exponent p such that base^p % mod == num % mod or
        // return -1 if it doesn't exist. Works for mod that fits in int.

        Long sqrt_pow = Math.ceil(Math.sqrt((double) mod));
        java.util.HashMap<Long, Long> baby_steps;
        Long base_to_sqrt_pow = 1;

        for (Long i = 0; i < sqrt_pow; i++)
        {
            base_to_sqrt_pow = base_to_sqrt_pow * base % mod;
        }

        for (Long i = 0, baby_step = num; i < sqrt_pow; i++)
        {
            // If baby_step already occurs in the hashmap, then it's value is
            // overwritten, thanks to which we find the smallest exponent later.
            baby_steps[baby_step] = i;
            baby_step = baby_step * base % mod;
        }

        for (Long i = 1, giant_step = base_to_sqrt_pow; i <= sqrt_pow; i++)
        {
            if (baby_steps.count(giant_step))
            {
                // The order in which we iterate over the giant steps finds the
                // smaller exponents first.
                return i * sqrt_pow - baby_steps[giant_step];
            }
        }

        return -1;
    }
    public static void main(String[] args)
    {

    }
}
