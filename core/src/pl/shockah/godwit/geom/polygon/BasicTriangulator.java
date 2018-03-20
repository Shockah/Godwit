package pl.shockah.godwit.geom.polygon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;

/*
 * code taken from Slick2D - http://slick.ninjacave.com/
 */
public class BasicTriangulator implements Triangulator {
	private static final float EPSILON = 0.0000000001f;
	private List<IVec2> poly = new ArrayList<>(), tris = new ArrayList<>();
	private boolean tried;

	@Override
	public void addPoint(@Nonnull IVec2 point) {
		if (!poly.contains(point))
			poly.add(point);
	}

	public int polyPointCount() {
		return poly.size();
	}

	public IVec2 polyPoint(int index) {
		return poly.get(index);
	}

	@Override
	public boolean triangulate() {
		tried = true;
		return process(poly.toArray(new IVec2[poly.size()]), tris);
	}

	@Override
	public int getTriangleCount() {
		if (!tried)
			triangulate();
		return tris.size() / 3;
	}

	@Override
	@Nonnull public IVec2 getTrianglePoint(int tri, int i) {
		if (!tried)
			triangulate();
		return tris.get(tri * 3 + i);
	}

	private float area(IVec2[] contour) {
		int n = contour.length;

		float A = 0f;
		for (int p = n - 1, q = 0; q < n; p = q++) {
			IVec2 contourP = contour[p];
			IVec2 contourQ = contour[q];
			A += contourP.x() * contourQ.y() - contourQ.x() * contourP.y();
		}
		return A * 0.5f;
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

	private boolean snip(IVec2[] contour, int u, int v, int w, int n, int[] V) {
		int p;
		double Ax, Ay, Bx, By, Cx, Cy, Px, Py;

		Ax = contour[V[u]].x();
		Ay = contour[V[u]].y();

		Bx = contour[V[v]].x();
		By = contour[V[v]].y();

		Cx = contour[V[w]].x();
		Cy = contour[V[w]].y();

		if (EPSILON > ((Bx - Ax) * (Cy - Ay)) - ((By - Ay) * (Cx - Ax)))
			return false;
		for (p = 0; p < n; p++) {
			if (p == u || p == v || p == w)
				continue;
			Px = contour[V[p]].x();
			Py = contour[V[p]].y();
			if (insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py))
				return false;
		}
		return true;
	}

	private boolean process(IVec2[] contour, List<IVec2> result) {
		result.clear();

		int n = contour.length;
		if (n < 3)
			return false;

		int[] V = new int[n];

		if (0f < area(contour)) {
			for (int v = 0; v < n; v++) {
				V[v] = v;
			}
		} else {
			for (int v = 0; v < n; v++) {
				V[v] = (n - 1) - v;
			}
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

				result.add(contour[a]);
				result.add(contour[b]);
				result.add(contour[c]);

				for (s = v, t = v + 1; t < nv; s++, t++) {
					V[s] = V[t];
				}
				nv--;
				count = 2 * nv;
			}
		}
		return true;
	}
}