#include <alloca.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <stdbool.h>
#include <math.h>

#define EPS (1e-6)
void panic() {
      fprintf(stderr, "PANIC");
      exit(1);
}
// safe allocations
void *allocate(size_t size) {
      void *ret = malloc(size);
      if (ret == NULL)
            panic();
      return ret;
}
typedef struct {
      double x;
      double y;
} point;
typedef struct {
      point a;
      point b;
} line;
// returns -1 when negative, 1 when positive, 0 when zero
int double_sign(double a) {
      if (fabs(a) < EPS)
            return 0;
      else if (a > 0)
            return 1;
      return -1;
}
enum sides {
      LEFT = 1,
      ON = 0,
      RIGHT = -1,
};
//returns the side of a plane where p is about the line l
int side(point p, line l) {
      point p3 = {p.x - l.a.x, p.y - l.a.y};
      point p2 = {l.b.x - l.a.x, l.b.y - l.a.y};
      return double_sign(p2.x * p3.y - p3.x * p2.y);
}

typedef struct {
      double r;
      double i;
} complex;
complex create(double a, double b) {
      complex ret = { a, b };
      return ret;
}
complex conjugate(complex a) {
      return create(a.r, -a.i);
}
complex inverse(complex a) {
      return create(a.r / (a.r * a.r + a.i * a.i), -a.i / (a.r * a.r + a.i * a.i));
}
complex multiply(complex a, complex b) {
      return create(a.r * b.r - a.i * b.i, a.r * b.i + a.i * b.r);
}
complex add(complex a, complex b) {
      return create(a.r + b.r, a.i + b.i);
}
complex subtract(complex a, complex b) {
      return create(a.r - b.r, a.i - b.i);
}
// returns a mirrored point p about the line l
point mirror_point(point p, line l) {
      complex pp = create(p.x, p.y);
      complex line_a = create(l.a.x, l.a.y);
      complex line_b_sub = subtract(create(l.b.x, l.b.y), line_a);

      // mirrored point is equal to the multiple of a conjugate of translated point
      pp = add(multiply(conjugate(multiply(subtract(pp, line_a), inverse(line_b_sub))), line_b_sub), line_a);
      return (point){pp.r, pp.i};
}
enum shape_types {
      RECTANGLE = 0,
      DISC = 1,
};
typedef union {
      struct {
            point center;
            double radius;
      } dis;
      struct {
            point left_down, right_up;
      } rec;
} shapes_union;

typedef struct {
      shapes_union cur;
      enum shape_types type;
} shapes;

// returns if the point p is inside the shape specified in `shape'
bool is_inside(shapes shape, point p) {
      if (shape.type == DISC) {
            double difx = shape.cur.dis.center.x - p.x;
            double dify = shape.cur.dis.center.y - p.y;
            if (dify * dify + difx * difx - EPS <= shape.cur.dis.radius * shape.cur.dis.radius)
                  return true;
            return false;
      }
      if (p.x + EPS < shape.cur.rec.left_down.x || p.x - EPS > shape.cur.rec.right_up.x ||
          p.y + EPS < shape.cur.rec.left_down.y || p.y - EPS > shape.cur.rec.right_up.y)
            return false;
      return true;
}

typedef struct {
      int last;
      shapes shape;
      line line;
} ll_element;

// Recursively goes through all the options and sums 1-es through them.
long long res(point query, int k, ll_element *linked_sheets) {
      if (linked_sheets[k].last != -1) {
            int s = side(query, linked_sheets[k].line);
            if (s == RIGHT)
                  return 0;
            long long sum = res(query, linked_sheets[k].last, linked_sheets);
            if (s != ON)
                  sum += res(mirror_point(query, linked_sheets[k].line), linked_sheets[k].last, linked_sheets);
            return sum;
      }
      return is_inside(linked_sheets[k].shape, query);
}

int main() {
      int n, q;
      if (scanf("%d %d\n", &n, &q) != 2)
            panic();
      // this is essentially a linked list of all sheets
      ll_element *linked_sheets = (ll_element*)allocate((size_t)n * sizeof(ll_element));

      for (int i = 0; i < n; i++) {
            char c;
            scanf("%c", &c);
            if (c == 'P') {
                  point a, b;
                  scanf("%lf %lf %lf %lf\n", &a.x, &a.y, &b.x, &b.y);
                  linked_sheets[i].last = -1;
                  linked_sheets[i].shape = (shapes) {
                        .cur.rec = {a, b},
                        .type = RECTANGLE,
                  };
            } else if (c == 'K') {
                  point a;
                  double r;
                  scanf("%lf %lf %lf\n", &a.x, &a.y, &r);
                  linked_sheets[i].last = -1;
                  linked_sheets[i].shape = (shapes) {
                        .cur.dis = {
                              .center = a,
                              .radius = r
                        },
                        .type = DISC
                  };
            } else {
                  int k;
                  point a, b;
                  scanf("%d %lf %lf %lf %lf\n", &k, &a.x, &a.y, &b.x, &b.y);
                  linked_sheets[i].last = k - 1;
                  linked_sheets[i].line = (line){a, b};
                  linked_sheets[i].shape = linked_sheets[k-1].shape;
            }
      }
      while (q--) {
            int k;
            point query;
            scanf("%d %lf %lf\n", &k, &query.x, &query.y);
            printf("%lld\n", res(query, k - 1, linked_sheets));
      }
      free(linked_sheets);
}
