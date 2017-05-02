package pl.shockah.godwit.geom.polygon;

import pl.shockah.godwit.geom.Vec2;

import java.util.ArrayList;
import java.util.List;

/*
 * code taken from Slick2D - http://slick.ninjacave.com/
 */
public class BasicTriangulator implements Triangulator {
    private static final float EPSILON = 0.0000000001f;
    private List<Vec2> poly = new ArrayList<>(), tris = new ArrayList<>();
    private boolean tried;

    @Override
    public void addPolyPoint(Vec2 point) {
        if (!poly.contains(point))
            poly.add(point);
    }

    public int polyPointCount() {
        return poly.size();
    }

    public Vec2 polyPoint(int index) {
        return poly.get(index);
    }

    @Override
    public boolean triangulate() {
        tried = true;
        return process(poly, tris);
    }

    @Override
    public int triangleCount() {
        if (!tried)
            triangulate();
        return tris.size() / 3;
    }

    @Override
    public Vec2 trianglePoint(int tri, int i) {
        if (!tried)
            triangulate();
        return tris.get(tri * 3 + i);
    }

    private float area(List<Vec2> contour) {
        int n = contour.size();

        float A = 0f;
        for (int p = n - 1, q = 0; q < n; p = q++) {
            Vec2 contourP = contour.get(p);
            Vec2 contourQ = contour.get(q);
            A += contourP.getX() * contourQ.getY() - contourQ.getX() * contourP.getY();
        }
        return A * .5f;
    }

    private boolean insideTriangle(double Ax, double Ay, double Bx, double By, double Cx, double Cy, double Px, double Py) {
        double ax, ay, bx, by, cx, cy, apx, apy, bpx, bpy, cpx, cpy, cCROSSap, bCROSScp, aCROSSbp;

        ax = Cx - Bx;
        ay = Cy - By;
        bx = Ax - Cx;
        by = Ay - Cy;
        cx = Bx - Ax;
        cy = By - Ay;
        apx = Px - Ax;
        apy = Py - Ay;
        bpx = Px - Bx;
        bpy = Py - By;
        cpx = Px - Cx;
        cpy = Py - Cy;

        aCROSSbp = ax * bpy - ay * bpx;
        cCROSSap = cx * apy - cy * apx;
        bCROSScp = bx * cpy - by * cpx;

        return aCROSSbp >= 0f && bCROSScp >= 0f && cCROSSap >= 0f;
    }

    private boolean snip(List<Vec2> contour, int u, int v, int w, int n, int[] V) {
        int p;
        double Ax, Ay, Bx, By, Cx, Cy, Px, Py;

        Ax = contour.get(V[u]).getX();
        Ay = contour.get(V[u]).getY();

        Bx = contour.get(V[v]).getX();
        By = contour.get(V[v]).getY();

        Cx = contour.get(V[w]).getX();
        Cy = contour.get(V[w]).getY();

        if (EPSILON > ((Bx - Ax) * (Cy - Ay)) - ((By - Ay) * (Cx - Ax)))
            return false;
        for (p = 0; p < n; p++) {
            if (p == u || p == v || p == w)
                continue;
            Px = contour.get(V[p]).getX();
            Py = contour.get(V[p]).getY();
            if (insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py))
                return false;
        }
        return true;
    }

    private boolean process(List<Vec2> contour, List<Vec2> result) {
        result.clear();

        int n = contour.size();
        if (n < 3)
            return false;

        int[] V = new int[n];

        if (0f < area(contour)) {
            for (int v = 0; v < n; v++)
                V[v] = v;
        } else {
            for (int v = 0; v < n; v++)
                V[v] = (n - 1) - v;
        }

        int nv = n;
        int count = 2 * nv;

        for (int v = nv - 1; nv > 2;) {
            if (0 >= count--)
                return false;

            int u = v;
            if (nv <= u)
                u = 0;
            v = u + 1;
            if (nv <= v)
                v = 0;
            int w = v + 1;
            if (nv <= w)
                w = 0;

            if (snip(contour, u, v, w, nv, V)) {
                int a, b, c, s, t;

                a = V[u];
                b = V[v];
                c = V[w];

                result.add(contour.get(a));
                result.add(contour.get(b));
                result.add(contour.get(c));

                for (s = v, t = v + 1; t < nv; s++, t++)
                    V[s] = V[t];
                nv--;
                count = 2*nv;
            }
        }
        return true;
    }
}