export interface AnalyticsSummary {
  totalReach: number;
  totalImpressions: number;
  totalLikes: number;
  totalComments: number;
  totalShares: number;
  totalFollowers: number;
  engagementRate: number;
}

export interface WeeklyEngagementDTO {
  day: string;
  likes: number;
  comments: number;
  shares: number;
}

export interface TopPostDTO {
  postId: string;
  content: string;
  likes: number;
  comments: number;
  shares: number;
  reach: number;
}

export interface ReachImpressionDTO {
  date: string;
  reach: number;
  impressions: number;
}

export interface FollowerGrowthDTO {
  date: string;
  newFollowers: number;
}

export interface DemographicsDTO {
  followersByRole: { [role: string]: number };
  totalConnections: number;
  pendingRequests: number;
  newConnectionsInPeriod: number;
  connectionAcceptanceRate: number;
}