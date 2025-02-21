#![allow(non_snake_case)]

use std::io::{stdin, BufRead, BufReader};

fn main() {
    let mut br = BufReader::new(stdin());

    let mut line = String::new();
    br.read_line(&mut line).unwrap();
    let mut split = line.split_whitespace();
    let T = split.next().unwrap().parse().unwrap();
    for tc in 1..=T {
        let mut line = String::new();
        br.read_line(&mut line).unwrap();
        let mut split = line.split_whitespace();
        let n = split.next().unwrap().parse().unwrap();
        let mut x = Vec::new();
        let mut y = Vec::new();
        for _ in 0..n {
            let mut line = String::new();
            br.read_line(&mut line).unwrap();
            let mut split = line.split_whitespace();
            x.push(split.next().unwrap().parse().unwrap());
            y.push(split.next().unwrap().parse().unwrap());
        }

        println!("Case #{}: {:.9}", tc, solve(&x, &y));
    }
}

fn solve(x: &[i32], y: &[i32]) -> f64 {
    let limit = (x.iter().max().unwrap() - x.iter().min().unwrap())
        .max(y.iter().max().unwrap() - y.iter().min().unwrap());

    let mut x_sorted_points: Vec<_> = (0..x.len()).map(|i| Point { x: x[i], y: y[i] }).collect();
    x_sorted_points.sort_by_key(|point| point.x);

    let mut y_sorted_indices: Vec<_> = (0..x_sorted_points.len()).collect();
    y_sorted_indices.sort_by_key(|&i| x_sorted_points[i].y);

    search(
        limit,
        &x_sorted_points,
        0,
        x_sorted_points.len() - 1,
        &y_sorted_indices,
    )
}

fn search(
    limit: i32,
    x_sorted_points: &[Point],
    begin_index: usize,
    end_index: usize,
    y_sorted_indices: &[usize],
) -> f64 {
    if end_index - begin_index + 1 < 3 {
        return std::f64::MAX;
    }

    let middle_index = (begin_index + end_index) / 2;

    let mut left_y_sorted_indices = Vec::new();
    let mut right_y_sorted_indices = Vec::new();
    for &y_sorted_index in y_sorted_indices {
        (if y_sorted_index <= middle_index {
            &mut left_y_sorted_indices
        } else {
            &mut right_y_sorted_indices
        })
        .push(y_sorted_index);
    }

    let mut result = search(
        limit,
        x_sorted_points,
        begin_index,
        middle_index,
        &left_y_sorted_indices,
    )
    .min(search(
        limit,
        x_sorted_points,
        middle_index + 1,
        end_index,
        &right_y_sorted_indices,
    ));

    let margin = result / 2.0;
    let split_x =
        ((x_sorted_points[middle_index].x + x_sorted_points[middle_index + 1].x) as f64) / 2.0;

    let mut start = 0;
    let mut near_indices = Vec::new();
    for i in 0..y_sorted_indices.len() {
        if ((x_sorted_points[y_sorted_indices[i]].x as f64) - split_x).abs() <= margin {
            while start != near_indices.len()
                && ((x_sorted_points[y_sorted_indices[i]].y
                    - x_sorted_points[near_indices[start] as usize].y) as f64)
                    > margin
            {
                start += 1;
            }

            for p in start..near_indices.len() {
                for q in p + 1..near_indices.len() {
                    result = result.min(compute_perimeter(
                        &x_sorted_points[y_sorted_indices[i]],
                        &x_sorted_points[near_indices[p]],
                        &x_sorted_points[near_indices[q]],
                    ));
                }
            }

            near_indices.push(y_sorted_indices[i]);
        }
    }

    result
}

fn compute_perimeter(p1: &Point, p2: &Point, p3: &Point) -> f64 {
    compute_distance(p1, p2) + compute_distance(p2, p3) + compute_distance(p3, p1)
}

fn compute_distance(p1: &Point, p2: &Point) -> f64 {
    ((((p1.x - p2.x) as i64).pow(2) + ((p1.y - p2.y) as i64).pow(2)) as f64).sqrt()
}

struct Point {
    x: i32,
    y: i32,
}
