import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  AnalyticsSummary,
  WeeklyEngagementDTO,
  TopPostDTO,
  ReachImpressionDTO,
  FollowerGrowthDTO,
  DemographicsDTO
} from './analytics.models';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {

  private readonly BASE_URL = 'http://localhost:8080/api/analytics';

  constructor(private http: HttpClient) {}

  getSummary(days: number): Observable<AnalyticsSummary> {
    const params = new HttpParams().set('days', days);
    return this.http.get<AnalyticsSummary>(`${this.BASE_URL}/summary`, { params });
  }

  getWeeklyEngagement(days: number): Observable<WeeklyEngagementDTO[]> {
    const params = new HttpParams().set('days', days);
    return this.http.get<WeeklyEngagementDTO[]>(`${this.BASE_URL}/weekly`, { params });
  }

  getTopPosts(days: number): Observable<TopPostDTO[]> {
    const params = new HttpParams().set('days', days);
    return this.http.get<TopPostDTO[]>(`${this.BASE_URL}/top-posts`, { params });
  }

  getReachImpressions(days: number): Observable<ReachImpressionDTO[]> {
    const params = new HttpParams().set('days', days);
    return this.http.get<ReachImpressionDTO[]>(`${this.BASE_URL}/reach`, { params });
  }

  getFollowerGrowth(days: number): Observable<FollowerGrowthDTO[]> {
    const params = new HttpParams().set('days', days);
    return this.http.get<FollowerGrowthDTO[]>(`${this.BASE_URL}/follower-growth`, { params });
  }

  
}